<div dir="rtl">

# Stage 4 - מערכת אבטחה מתקדמת עם הפרדת תפקידים

## סקירה כללית - מה השתנה ב-Stage 4?

ב-Stage 4 המערכת מתקדמת יותר עם הפרדה ברורה בין סוגי משתמשים ודפים ייעודיים. השיפורים העיקריים:

- **הפרדת דפי בית** - דף נפרד למנהלים (`admin_home/`)
- **הרשאות מדורגות** - הבחנה בין endpoints שצריכים USER ו-ADMIN
- **ניהול התחברות משופר** - דף login מותאם וlogout עם הודעות
- **לוגים מפורטים** - מעקב אחר פעילות המשתמשים

## ארכיטקטורת המערכת המשופרת

</div>

```mermaid
graph TB
    A[Client Browser] --> B[Spring Security Filter Chain]
    B --> C[Enhanced Authorization Rules]
    C --> D[Controllers Layer]
    D --> E[Services Layer]
    
    C --> C1["/admin_home, /role, /register - ADMIN only"]
    C --> C2["/home - USER or ADMIN"]
    C --> C3["/login, /hello, /status - Authenticated"]
    
    D --> D1[UserController - Enhanced]
    D --> D2[RoleController]
    
    D1 --> D1A["/admin_home - Admin Dashboard"]
    D1 --> D1B["/home - General Home"]
    D1 --> D1C["/login - Login Page"]
    
    E --> E1[UserServiceImpl]
    E --> E2[RoleServiceImpl]
    E --> E3[CustomUserDetailsService - With Logging]
    
%%    style C fill:#ffcccc
%%    style C1 fill:#ff6b6b
%%    style C2 fill:#ffd93d
%%    style C3 fill:#6bcf7f
%%    style E3 fill:#74c0fc
```

<div dir="rtl">

## הרשאות מתקדמות ב-SecurityConfig

המערכת עכשיו מגדירה שלוש רמות הרשאה ברורות:

</div>

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/role", "/register", "/admin_home").hasRole("ADMIN") // Admin only
    .requestMatchers("/home").hasAnyRole("USER", "ADMIN") // User or Admin  
    .anyRequest().authenticated()) // All others need authentication
```

<div dir="rtl">

### מפת ההרשאות החדשה:

</div>

```mermaid
graph LR
    A[Endpoints Authorization Map] --> B[ADMIN Only]
    A --> C[USER or ADMIN]
    A --> D[Any Authenticated]
    
    B --> B1["/role"]
    B --> B2["/register"] 
    B --> B3["/admin_home"]
    
    C --> C1["/home"]
    C --> C2["/"]
    
    D --> D1["/hello"]
    D --> D2["/status"]
    D --> D3["/login"]
    D --> D4["/logout"]
    
%%    style B fill:#ffcccc
%%    style C fill:#ffffcc
%%    style D fill:#ccffcc
```

<div dir="rtl">

## Controllers משופרים

### UserController עם Endpoints חדשים

הוספו שני endpoints חשובים:

</div>

```java
@GetMapping("/admin_home")
public ResponseEntity<StandardResponse> adminHome() {
    StandardResponse response = new StandardResponse("success", "Welcome to the admin home page!", null);
    return ResponseEntity.ok(response);
}

@GetMapping("/login")
public ResponseEntity<StandardResponse> showLogin() {
    StandardResponse response = new StandardResponse("success", "Please login", null);
    return ResponseEntity.ok(response);
}
```

<div dir="rtl">

### זרימת הניווט החדשה:

</div>

```mermaid
graph TD
    A[User Access] --> B{User Role?}
    B -->|ADMIN| C[Can access /admin_home]
    B -->|USER| D[Cannot access /admin_home]
    B -->|ADMIN| E[Can access /home]
    B -->|USER| F[Can access /home]
    
    C --> G[Admin Dashboard Features]
    D --> H[403 Forbidden]
    E --> I[General Home Page]
    F --> I
    
%%    style C fill:#e8f5e8
%%    style E fill:#e8f5e8
%%    style F fill:#e8f5e8
%%    style D fill:#ffebee
%%    style H fill:#ffcdd2
```

<div dir="rtl">

## CustomUserDetailsService עם לוגים

השירות עכשיו כולל לוגים מפורטים למעקב:

</div>

```java
@Override
@Transactional
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findById(username);
    User user = userOptional.orElse(null);
    if (user == null) {
        throw new UsernameNotFoundException("Invalid username.");
    }

    logger.info("Loaded user: {}", user.getUsername());
    logger.info("User roles: {}", user.getRoles());

    return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            mapRolesToAuthorities(user.getRoles())
    );
}
```

<div dir="rtl">

### זרימת אימות עם לוגים:

</div>

```mermaid
sequenceDiagram
    participant User as User
    participant SC as SecurityConfig
    participant CUD as CustomUserDetailsService
    participant UR as UserRepository
    participant Log as Logger
    
    User->>SC: Login attempt
    SC->>CUD: loadUserByUsername(username)
    CUD->>UR: findById(username)
    UR-->>CUD: User entity
    
    CUD->>Log: Log: "Loaded user: username"
    CUD->>Log: Log: "User roles: [ADMIN/USER]"
    
    CUD->>CUD: mapRolesToAuthorities()
    CUD-->>SC: UserDetails with authorities
    SC-->>User: Authentication success/failure
```

<div dir="rtl">

## ניהול Logout משופר

הlogout עכשיו מפנה לדף login עם הודעה:

</div>

```java
.logout(logout -> logout
    .logoutUrl("/logout")
    .logoutSuccessUrl("/login?logout=true")  // Improved logout messaging
    .deleteCookies("JSESSIONID")
    .permitAll())
```

<div dir="rtl">

### זרימת Logout משופרת:

</div>

```mermaid
graph TD
    A[User clicks logout] --> B[POST /logout]
    B --> C[Spring Security LogoutFilter]
    C --> D[Delete JSESSIONID cookie]
    D --> E[Clear SecurityContext]
    E --> F[Redirect to /login?logout=true]
    F --> G[UserController.showLogin ]
    G --> H[Display: Please login - with logout parameter]
    
%%    style F fill:#e3f2fd
%%    style H fill:#e8f5e8
```

<div dir="rtl">

## תרחישי שימוש מפורטים

### תרחיש 1: מנהל נכנס למערכת

</div>

```mermaid
sequenceDiagram
    participant Admin as Admin User
    participant Browser as Browser
    participant SC as SecurityConfig
    participant UC as UserController
    participant CUD as CustomUserDetailsService
    
    Admin->>Browser: Navigate to /login
    Browser->>SC: POST /login (admin credentials)
    SC->>CUD: loadUserByUsername("admin")
    
    Note over CUD: Log: "Loaded user: admin"
    Note over CUD: Log: "User roles: [ADMIN]"
    
    CUD-->>SC: UserDetails with ROLE_ADMIN
    SC-->>Browser: Redirect to /home (success)
    
    Admin->>Browser: Navigate to /admin_home
    Browser->>UC: GET /admin_home
    
    Note over UC: Check: hasRole("ADMIN") - PASS
    UC-->>Browser: "Welcome to the admin home page!"
    
    Admin->>Browser: Create new role
    Browser->>UC: POST /role
    Note over UC: Check: hasRole("ADMIN") - PASS
    UC-->>Browser: Role created successfully
```

<div dir="rtl">

### תרחיש 2: משתמש רגיל מנסה לגשת לדף מנהל

</div>

```mermaid
sequenceDiagram
    participant User as Regular User
    participant Browser as Browser
    participant SC as SecurityConfig
    participant UC as UserController
    
    User->>Browser: Login with user credentials
    Browser->>SC: Authentication successful with ROLE_USER
    
    User->>Browser: Try to access /admin_home
    Browser->>SC: GET /admin_home
    
    Note over SC: Check: hasRole("ADMIN")?
    Note over SC: User has ROLE_USER - DENIED
    
    SC-->>Browser: 403 Forbidden
    Browser-->>User: Access Denied Error
    
    User->>Browser: Navigate to /home (allowed)
    Browser->>UC: GET /home
    
    Note over UC: Check: hasAnyRole("USER", "ADMIN") - PASS
    UC-->>Browser: "Welcome to the home page!"
```

<div dir="rtl">

### תרחיש 3: זרימה מלאה עם Logout

</div>

```mermaid
graph TD
    A[User logs in] --> B{Authentication successful?}
    B -->|No| C[Redirect to /login?error=true]
    B -->|Yes| D{User role?}
    
    D -->|ADMIN| E[Access to all pages]
    D -->|USER| F[Limited access]
    
    E --> G["/admin_home available"]
    E --> H["/home available"] 
    F --> I["/admin_home - 403 Forbidden"]
    F --> H
    
    G --> J[User clicks logout]
    H --> J
    J --> K[POST /logout]
    K --> L[Clear session & cookies]
    L --> M[Redirect to /login?logout=true]
    M --> N[Show login page with logout message]
    
%%    style E fill:#e8f5e8
%%    style F fill:#fff3e0
%%    style I fill:#ffebee
%%    style N fill:#e3f2fd
```

<div dir="rtl">

## השיפורים ב-UserServiceImpl

שונה מ-`existsByUsername` ל-`existsById` לעקביות:

</div>

```java
public UserResponseDto registerUser(UserDto userDto) {
    // Check if user already exists - improved method
    if (userRepository.existsById(userDto.getUsername())) {
        throw new InvalidRequestException("User already exists: " + userDto.getUsername());
    }
    // ... rest of method
}
```

<div dir="rtl">

## מיפוי נקודות הקצה המלא

</div>

```mermaid
graph LR
    A[HTTP Endpoints] --> B[Public Access]
    A --> C[Authenticated Users]
    A --> D[USER Role]
    A --> E[ADMIN Role]
    
    C --> C1["/hello"]
    C --> C2["/status"]
    C --> C3["/login"]
    C --> C4["/logout"]
    
    D --> D1["/home"]
    D --> D2["/"]
    
    E --> E1["/admin_home"]
    E --> E2["/role"]
    E --> E3["/register"]
    E --> E4["All USER endpoints"]
    
%%    style B fill:#e8f5e8
%%    style C fill:#fff3e0
%%    style D fill:#e3f2fd
%%    style E fill:#ffebee
```

<div dir="rtl">

## מטריקות אבטחה ומעקב

עם הלוגים החדשים, אפשר לעקוב אחר:

</div>

```java
// In CustomUserDetailsService
logger.info("Loaded user: {}", user.getUsername());
logger.info("User roles: {}", user.getRoles());

// What you'll see in logs:
// INFO - Loaded user: admin
// INFO - User roles: [Role(id=1, name=ADMIN)]
// INFO - Loaded user: user  
// INFO - User roles: [Role(id=2, name=USER)]
```

<div dir="rtl">

### דשבורד מעקב (התיאורטי):

</div>

```mermaid
graph TD
    A[Security Monitoring] --> B[Login Attempts]
    A --> C[Role Access Patterns]
    A --> D[Failed Authorization]
    
    B --> B1[Successful logins by role]
    B --> B2[Failed login attempts]
    B --> B3[Login frequency]
    
    C --> C1[ADMIN accessing /admin_home]
    C --> C2[USER accessing /home]
    C --> C3[Cross-role access attempts]
    
    D --> D1[403 Forbidden responses]
    D --> D2[Unauthorized endpoint access]
    D --> D3[Security violations]
    
%%    style A fill:#e1f5fe
%%    style D fill:#ffebee
```

<div dir="rtl">

## Best Practices שהמערכת מיישמת

### 1. Principle of Least Privilege
כל משתמש מקבל רק את ההרשאות המינימליות הנדרשות:
- USER - גישה לדפי בית בסיסיים
- ADMIN - גישה מלאה לניהול + כל הרשאות USER

### 2. Clear Separation of Concerns
הפרדה ברורה בין:
- דפי ניהול (`admin_home/`)
- דפי משתמש רגיל (`home/`)
- פעולות ניהול (`role`, `/register/`)

### 3. Comprehensive Logging
מעקב מלא אחר פעילות אבטחה

### 4. Graceful Error Handling
הודעות ברורות למשתמש (login, logout, errors)

## הרצת המערכת - משתמשי ברירת מחדל

המערכת מגיעה עם שני משתמשים:

</div>

```java
// From DataInitializer (assumed similar to Stage 3)
// admin / admin - ROLE_ADMIN
// user / user - ROLE_USER
```

<div dir="rtl">

### מסלולי בדיקה מומלצים:

1. **התחברות כ-admin:**
    - גישה ל-`admin_home/` (מותר)
    - יצירת תפקיד ב-`role/` (מותר)
    - רישום משתמש ב-`register/` (מותר)

2. **התחברות כ-user:**
    - גישה ל-`home/` (מותר)
    - ניסיון גישה ל-`admin_home` (403 Forbidden)
    - ניסיון יצירת תפקיד (403 Forbidden)

3. **בדיקת logout:**
    - logout מכל משתמש
    - וידוא הפניה ל-`login?logout=true/`

## סיכום השיפורים ב-Stage 4

המערכת עכשיו מספקת:
- **הרשאות מדורגות** עם הפרדה ברורה
- **ניהול משתמשים מתקדם** עם דפים ייעודיים
- **מעקב ולוגים** לפעילות אבטחה
- **חוויית משתמש משופרת** עם הודעות ברורות
- **אבטחה רובסטית** עם בדיקות הרשאה מחמירות

זה מהווה בסיס איתן למערכת אבטחה ארגונית שאפשר להרחיב ולפתח הלאה.

</div>