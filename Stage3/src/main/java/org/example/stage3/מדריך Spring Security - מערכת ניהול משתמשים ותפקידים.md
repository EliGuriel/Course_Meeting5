# מדריך Spring Security - מערכת ניהול משתמשים ותפקידים

<div dir="rtl">

## סקירה כללית של המערכת

המערכת בנויה על Spring Boot עם Spring Security ומממשת מערכת אימות בסיסית עם ניהול תפקידים. המערכת כוללת שני תפקידים עיקריים: ADMIN ו-USER, כאשר למנהלים יש הרשאות מיוחדות ליצירת תפקידים ורישום משתמשים חדשים.

## מבנה כללי של האפליקציה

</div>

```mermaid
graph TB
    A[Client Browser] --> B[Spring Security Filter Chain]
    B --> C[Controllers Layer]
    C --> D[Services Layer]
    D --> E[Repository Layer]
    E --> F[Database]
    
    C1[UserController] --> S1[UserService]
    C2[RoleController] --> S2[RoleService]
    
    S1 --> R1[UserRepository]
    S2 --> R2[RoleRepository]
    
    SEC[SecurityConfig] --> B
    CUD[CustomUserDetailsService] --> S1
    
%%    style SEC fill:#ffcccc
%%    style CUD fill:#ccffcc
```

<div dir="rtl">

## תצורת האבטחה - SecurityConfig

הקובץ `SecurityConfig` הוא הלב של מערכת האבטחה. בואו נבין את כל רכיביו:

### מבנה תצורת האבטחה

</div>

```mermaid
graph TD
    A[SecurityConfig] --> B[PasswordEncoder Bean]
    A --> C[SecurityFilterChain Bean]
    
    C --> D[CSRF Protection - DISABLED]
    C --> E[Authorization Rules]
    C --> F[Session Management]
    C --> G[Form Login Configuration]
    C --> H[Logout Configuration]
    
    E --> E1["/role" - ADMIN only]
    E --> E2["/register" - ADMIN only]
    E --> E3["All other requests - Authenticated"]
    
    G --> G1["Login URL: /login"]
    G --> G2["Success URL: /home"]
    G --> G3["Failure URL: /login?error=true"]
    
    H --> H1["Logout URL: /logout"]
    H --> H2["Success URL: /hello"]
    H --> H3["Delete Cookies: JSESSIONID"]
    
%%    style A fill:#ffcccc
%%    style E fill:#ffffcc
```

<div dir="rtl">

### הסבר מפורט על רכיבי האבטחה

#### 1. מקודד סיסמאות (PasswordEncoder)
הקוד משתמש ב-`BCryptPasswordEncoder` עם חוזק 12 במקום ברירת המחדל 10. זה מספק אבטחה טובה יותר:

</div>

```java
@Bean
public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
}
```

<div dir="rtl">

#### 2. כללי הרשאות (Authorization Rules)
המערכת מגדירה שלושה רמות הרשאה:
- `role/` ו-`register/` - דורשים תפקיד ADMIN
- כל שאר הבקשות - דורשות אימות בלבד

#### 3. ניהול סשן (Session Management)
הוגדר ל-`IF_REQUIRED` - יוצר סשן רק כאשר צריך.

#### 4. תצורת התחברות (Form Login)
- נקודת קצה לכניסה: `login/`
- הפניה בהצלחה: `home/`
- הפניה בכישלון: `login?error=true/`

## זרימת תהליך האימות

</div>

```mermaid
sequenceDiagram
    participant U as User
    participant SC as SecurityConfig
    participant CUD as CustomUserDetailsService
    participant UR as UserRepository
    participant DB as Database
    
    U->>+SC: Login Request (/login)
    SC->>+CUD: loadUserByUsername()
    CUD->>+UR: findById(username)
    UR->>+DB: Query User
    DB-->>-UR: User Entity
    UR-->>-CUD: User Object
    
    alt User Not Found
        CUD-->>SC: UsernameNotFoundException
        SC-->>U: Redirect to /login?error=true
    else User Found
        CUD->>CUD: mapRolesToAuthorities()
        CUD-->>-SC: UserDetails Object
        SC->>SC: Password Verification
        
        alt Password Valid
            SC-->>U: Redirect to /home
        else Password Invalid
            SC-->>U: Redirect to /login?error=true
        end
    end
```

<div dir="rtl">

## שירות פרטי לטעינת פרטי משתמש - CustomUserDetailsService

השירות הזה אחראי על טעינת פרטי המשתמש מהמסד נתונים וממיר אותם לפורמט שSpring Security מבין:

### תהליך טעינת משתמש

</div>

```mermaid
graph TD
    A[loadUserByUsername] --> B{User exists?}
    B -->|No| C[Throw UsernameNotFoundException]
    B -->|Yes| D[Create UserDetails]
    D --> E[mapRolesToAuthorities]
    E --> F[Add ROLE_ prefix]
    F --> G[Return UserDetails]
    
    G --> H{User enabled?}
    H -->|No| I[Throw Exception - User locked]
    H -->|Yes| J{Account expired?}
    J -->|Yes| K[Throw Exception - Account expired]
    J -->|No| L[Authentication Success]
    
%%    style A fill:#ccffcc
%%    style E fill:#ffffcc
```

<div dir="rtl">

### המרת תפקידים לרשויות

"השירות ממיר תפקידים לרשויות על ידי הוספת הקידומת "_ROLE":

</div>

```java
private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
        .collect(Collectors.toList());
}
```

<div dir="rtl">

## בקרים (Controllers)

### UserController - בקר המשתמשים

הבקר מספק מספר נקודות קצה:

</div>

```mermaid
graph LR
    A[UserController] --> B[GET /hello - Public greeting]
    A --> C[GET /home, / - Home page]
    A --> D[GET /status - Current user info]
    A --> E[POST /register - User registration - ADMIN only]
    
%%    style E fill:#ffcccc
```

<div dir="rtl">

#### נקודות קצה מפורטות:

1. **`hello/`** - מחזיר ברכה פשוטה
2. **`home/`, `/`** - עמוד הבית לאחר התחברות מוצלחת
3. **`status/`** - מציג את שם המשתמש הנוכחי
4. **`register/`** - רישום משתמש חדש (ADMIN בלבד)

### RoleController - בקר התפקידים

</div>

```mermaid
graph LR
    A[RoleController] --> B[POST /role - Create new role - ADMIN only]
    
%%    style B fill:#ffcccc
```

<div dir="rtl">

הבקר מאפשר יצירת תפקידים חדשים במערכת, אך רק למשתמשים עם תפקיד ADMIN.

## שירותים (Services)

### UserService - שירות המשתמשים

</div>

```mermaid
graph TD
    A[UserServiceImpl] --> B[registerUser]
    A --> C[getCurrentUsername]
    
    B --> D{User exists?}
    D -->|Yes| E[Throw InvalidRequestException]
    D -->|No| F[Create User]
    F --> G[Encode Password]
    G --> H[Assign Roles]
    H --> I{Roles found?}
    I -->|No| J[Throw Exception]
    I -->|Yes| K[Save User]
    K --> L[Return UserResponseDto]
    
    C --> M[Get SecurityContext]
    M --> N[Return Username]
    
%%    style B fill:#ccffcc
%%    style C fill:#ffffcc
```

<div dir="rtl">

#### תהליך רישום משתמש:
1. בדיקה אם המשתמש כבר קיים
2. יצירת אובייקט משתמש חדש
3. הצפנת הסיסמה
4. הקצאת תפקידים
5. שמירה במסד הנתונים

### RoleService - שירות התפקידים

</div>

```mermaid
graph TD
    A[RoleServiceImpl] --> B[addRole]
    B --> C{Role exists?}
    C -->|Yes| D[Throw InvalidRequestException]
    C -->|No| E[Convert to uppercase]
    E --> F[Save Role]
    F --> G[Return RoleDto]
    
%%    style B fill:#ccffcc
```

<div dir="rtl">

## אתחול נתונים - DataInitializer

המחלקה הזו מאתחלת את המסד נתונים עם נתונים בסיסיים:

</div>

```mermaid
graph TD
    A[DataInitializer] --> B[initRoles]
    A --> C[initUsers]
    
    B --> D[Create ADMIN role]
    B --> E[Create USER role]
    
    C --> F[Create admin user]
    C --> G[Create regular user]
    
    F --> H[Username: admin, Password: admin, Role: ADMIN]
    G --> I[Username: user, Password: user, Role: USER]
    
%%    style A fill:#e6ffe6
```

<div dir="rtl">

### משתמשי ברירת מחדל:
- **admin/admin** - משתמש עם הרשאות מנהל
- **user/user** - משתמש רגיל

## זרימת עבודה מלאה

</div>

```mermaid
sequenceDiagram
    participant Admin as Admin User
    participant Browser as Browser
    participant SC as Security Config
    participant UC as UserController
    participant US as UserService
    participant RC as RoleController
    participant RS as RoleService
    participant DB as Database
    
    Admin->>Browser: Navigate to /login
    Browser->>SC: POST /login (admin/admin)
    SC->>DB: Authenticate user
    DB-->>SC: User authenticated with ADMIN role
    SC-->>Browser: Redirect to /home
    
    Admin->>Browser: Create new role
    Browser->>RC: POST /role (roleName: "MANAGER")
    RC->>RS: addRole("MANAGER")
    RS->>DB: Save new role
    DB-->>RS: Role saved
    RS-->>RC: Return RoleDto
    RC-->>Browser: 201 Created
    
    Admin->>Browser: Register new user
    Browser->>UC: POST /register (user data)
    UC->>US: registerUser(userDto)
    US->>DB: Save new user with roles
    DB-->>US: User saved
    US-->>UC: Return UserResponseDto
    UC-->>Browser: 201 Created
```

<div dir="rtl">

## נקודות חשובות לאבטחה

### 1. הצפנת סיסמאות
המערכת משתמשת ב-BCrypt עם חוזק 12, שמספק רמת אבטחה גבוהה.

### 2. הפרדת תפקידים
- ADMIN - יכול ליצור תפקידים ולרשום משתמשים
- USER - גישה בסיסית למערכת

### 3. הגנת CSRF
מושבת בשלב זה, אך בסביבת ייצור יש לשקול הפעלה.

### 4. ניהול סשנים
המערכת יוצרת סשן רק כאשר נדרש, מה שחוסך משאבים.

### 5. בדיקות תקינות
המערכת בודקת שהמשתמש קיים, פעיל ולא פג תוקפו.



המערכת מספקת בסיס איתן לאימות ואישור במערכת Spring. היא כוללת:
- מנגנון אימות מובנה
- ניהול תפקידים מתקדם
- הגנה על נקודות קצה רגישות
- אתחול אוטומטי של נתונים בסיסיים

המבנה מאפשר הרחבה קלה והוספת תכונות אבטחה נוספות בעתיד.

</div>