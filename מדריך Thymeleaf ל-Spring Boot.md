<div dir="rtl">

# מדריך Thymeleaf ל-Spring Boot

## מה זה Thymeleaf?

Thymeleaf הוא מנוע תבניות (template engine) לשרת Java המיועד ליצירת תוכן HTML, XML, JavaScript, CSS ותוכן טקסטואלי. הוא נבנה במיוחד כדי להשתלב עם Spring Boot, אם כי ניתן להשתמש בו גם עם פלטפורמות אחרות.

בניגוד למנועי תבניות אחרים, Thymeleaf מציע גישה ייחודית: הקבצים שלו הם תבניות חוקיות לגמרי של HTML שניתן להציג ישירות בדפדפן, גם ללא עיבוד בצד השרת. זה הופך את הקבצים לקריאים יותר למפתחים ומעצבים ומאפשר שיתוף פעולה טוב יותר בין צוותי פיתוח וצוותי עיצוב.

## מה מיוחד ב-Thymeleaf?

1. **תבניות טבעיות (Natural Templates)**: התבניות הן קבצי HTML תקינים שניתן לפתוח ולצפות בהם ישירות בדפדפן.
2. **אינטגרציה מובנית עם Spring**: Thymeleaf תוכנן לעבוד בצורה חלקה עם Spring MVC ו-Spring Boot.
3. **דיאלקטים מורחבים**: מאפשר הרחבה והתאמה אישית של פונקציונליות לפי הצורך.
4. **תמיכה בעיבוד פרגמנטים**: יכולת לעבד חלקים מהתבנית במקום את כולה, שימושי ליצירת דפים דינמיים.
5. **מודולריות**: אפשרות לשימוש חוזר בחלקים של תבניות (layouts, fragments).
6. **תמיכה בשפות שונות**: תמיכה מובנית ב-i18n (Internationalization) ותרגומים.

## התקנה והגדרה בסיסית ב-Spring Boot

### הוספת התלות ל-pom.xml (Maven)

</div>

```xml
<!-- Add Thymeleaf dependency to your Maven project -->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

<div dir="rtl">

### הגדרות ב-application.properties

</div>

```properties
# Template files location
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# Cache setting (disable during development)
spring.thymeleaf.cache=false

# Template mode (HTML5 by default)
spring.thymeleaf.mode=HTML
```

<div dir="rtl">

## מבנה פרויקט בסיסי

</div>

```
src
└── main
    ├── java
    │   └── com.example.demo
    │       ├── DemoApplication.java
    │       ├── controller
    │       │   └── HomeController.java
    │       └── model
    │           └── User.java
    └── resources
        ├── static
        │   ├── css
        │   └── js
        ├── templates
        │   ├── fragments
        │   │   ├── footer.html
        │   │   └── header.html
        │   ├── home.html
        │   └── layout.html
        └── application.properties
```

<div dir="rtl">

## בקר בסיסי ב-Spring MVC

</div>

```java
// Basic controller that returns a view with model attributes
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "ברוכים הבאים ל-Thymeleaf!");
        model.addAttribute("currentDate", new Date());
        return "home";  // Will resolve to templates/home.html
    }
}
```

<div dir="rtl">

## שימוש בסיסי ב-Thymeleaf

### תבנית HTML בסיסית

</div>

```html
<!DOCTYPE html>
<!-- Define Thymeleaf namespace, right-to-left direction and Hebrew language -->
<html xmlns:th="http://www.thymeleaf.org" dir="rtl" lang="he">
<head>
    <meta charset="UTF-8">
    <title>דוגמה בסיסית</title>
</head>
<body>
    <!-- Use th:text to replace content with a model attribute -->
    <h1 th:text="${message}">זוהי כותרת ברירת מחדל</h1>
    <!-- Format date using Thymeleaf's date utilities -->
    <p>היום הוא: <span th:text="${#dates.format(currentDate, 'dd/MM/yyyy')}">01/01/2023</span></p>
</body>
</html>
```

<div dir="rtl">

## תחביר וביטויים שימושיים ב-Thymeleaf

### משתנים וביטויים

</div>

| תחביר | תיאור | דוגמה |
|-------|-------|-------|
| `${...}` | ביטוי משתנה (Variable Expression) | `<p th:text="${user.name}">שם משתמש</p>` |
| `*{...}` | ביטוי בחירה (Selection Expression) | `<div th:object="${user}"><p th:text="*{name}">שם</p></div>` |
| `#{...}` | ביטוי הודעה (Message Expression) למטרות תרגום | `<p th:text="#{welcome.message}">ברוכים הבאים</p>` |
| `@{...}` | ביטוי קישור (Link Expression) | `<a th:href="@{/user/{id}(id=${user.id})}">פרופיל</a>` |
| `~{...}` | ביטוי פרגמנט (Fragment Expression) | `<div th:insert="~{fragments/header :: header}">כותרת</div>` |

<div dir="rtl">

### תנאים

</div>

```html
<!-- if condition: only show content if user is admin -->
<p th:if="${user.isAdmin()}">ברוכים הבאים, מנהל!</p>

<!-- unless condition (opposite of if) -->
<p th:unless="${user.isAdmin()}">ברוכים הבאים, משתמש רגיל</p>

<!-- switch-case statement -->
<div th:switch="${user.role}">
    <p th:case="'admin'">מנהל מערכת</p>
    <p th:case="'manager'">מנהל</p>
    <p th:case="*">משתמש רגיל</p>  <!-- default case -->
</div>
```

<div dir="rtl">

### לולאות

</div>

```html
<!-- Iterate over a list of users -->
<ul>
    <li th:each="user : ${users}" th:text="${user.name}">שם משתמש</li>
</ul>

<!-- With iteration status information -->
<ul>
    <li th:each="user, iterStat : ${users}">
        <span th:text="${iterStat.index + 1}">1</span>.
        <span th:text="${user.name}">שם משתמש</span>
        <span th:if="${iterStat.odd}">(אי-זוגי)</span>
        <span th:if="${iterStat.even}">(זוגי)</span>
    </li>
</ul>
```

<div dir="rtl">

### מבני עזר (Fragments) ושימוש חוזר

#### הגדרת פרגמנט (fragments/header.html)

</div>

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
</head>
<body>
    <!-- Define a reusable fragment named "header" -->
    <header th:fragment="header">
        <h1>כותרת האתר שלי</h1>
        <nav>
            <ul>
                <li><a th:href="@{/}">בית</a></li>
                <li><a th:href="@{/about}">אודות</a></li>
                <li><a th:href="@{/contact}">צור קשר</a></li>
            </ul>
        </nav>
    </header>
</body>
</html>
```

<div dir="rtl">

#### שימוש בפרגמנט

</div>

```html
<!-- Insert method - inserts fragment content inside the host element -->
<div th:insert="~{fragments/header :: header}">תוכן זה יוחלף</div>

<!-- Replace method - replaces the host element with the fragment -->
<div th:replace="~{fragments/header :: header}">תוכן זה יוחלף</div>

<!-- Include method - inserts fragment content without its root tag -->
<div th:include="~{fragments/header :: header}">תוכן זה יוחלף</div>
```

<div dir="rtl">

### פונקציות שימושיות

#### עיבוד תאריכים

</div>

```html
<!-- Format date with pattern -->
<p th:text="${#dates.format(currentDate, 'dd/MM/yyyy')}">01/01/2023</p>

<!-- Get day of month -->
<p th:text="${#dates.day(currentDate)}">1</p>

<!-- Get month of year -->
<p th:text="${#dates.month(currentDate)}">1</p>

<!-- Get year -->
<p th:text="${#dates.year(currentDate)}">2023</p>
```

<div dir="rtl">

#### עיבוד מחרוזות

</div>

```html
<!-- Get string length -->
<p th:text="${#strings.length(name)}">0</p>

<!-- Check if string is empty -->
<p th:if="${#strings.isEmpty(name)}">אין שם</p>

<!-- Check if string contains substring -->
<p th:if="${#strings.contains(name, 'ישראל')}">שם ישראלי</p>

<!-- Convert to uppercase -->
<p th:text="${#strings.toUpperCase(name)}">שם</p>

<!-- Get substring -->
<p th:text="${#strings.substring(name, 0, 5)}">חלק מהשם</p>
```

<div dir="rtl">

#### עיבוד אוספים

</div>

```html
<!-- Get collection size -->
<p th:text="${#lists.size(users)}">0</p>

<!-- Check if collection is empty -->
<p th:if="${#lists.isEmpty(users)}">אין משתמשים</p>

<!-- Check if collection contains element -->
<p th:if="${#lists.contains(roles, 'ADMIN')}">יש מנהל מערכת</p>
```

<div dir="rtl">

### טיפול בטפסים

#### טופס בסיסי

</div>

```html
<!-- Form with model binding and validation -->
<form th:action="@{/register}" th:object="${user}" method="post">
    <div>
        <label for="username">שם משתמש:</label>
        <input type="text" id="username" th:field="*{username}" />
        <!-- Display validation errors if present -->
        <span th:if="${#fields.hasErrors('username')}" th:errors="*{username}">שגיאת שם משתמש</span>
    </div>
    <div>
        <label for="email">אימייל:</label>
        <input type="email" id="email" th:field="*{email}" />
        <span th:if="${#fields.hasErrors('email')}" th:errors="*{email}">שגיאת אימייל</span>
    </div>
    <div>
        <label for="password">סיסמה:</label>
        <input type="password" id="password" th:field="*{password}" />
        <span th:if="${#fields.hasErrors('password')}" th:errors="*{password}">שגיאת סיסמה</span>
    </div>
    <button type="submit">הרשמה</button>
</form>
```

<div dir="rtl">

### תמיכה בלוקליזציה (i18n)

#### קובץ הודעות (messages.properties)

</div>

```properties
# Example messages for internationalization
welcome.message=ברוכים הבאים לאתר שלנו
user.greeting=שלום, {0}!
```

<div dir="rtl">

#### שימוש בהודעות

</div>

```html
<!-- Using message expressions for internationalization -->
<h1 th:text="#{welcome.message}">ברוכים הבאים</h1>
<p th:text="#{user.greeting(${user.name})}">שלום, משתמש!</p>
```

<div dir="rtl">

## טיפים מתקדמים

### יצירת Layout עם פרגמנטים

#### הגדרת Layout (templates/layout.html)

</div>

```html
<!DOCTYPE html>
<!-- Base layout template -->
<html xmlns:th="http://www.thymeleaf.org" dir="rtl" lang="he">
<head>
    <meta charset="UTF-8">
    <!-- This will be replaced by the page title -->
    <title th:replace="~{::title}">כותרת ברירת מחדל</title>
    <!-- Common CSS files -->
    <link rel="stylesheet" th:href="@{/css/main.css}">
    <!-- Placeholder for additional scripts/styles -->
    <th:block th:replace="~{::head-additional}"></th:block>
</head>
<body>
    <!-- Header section -->
    <header th:replace="~{fragments/header :: header}">כותרת עליונה</header>
    
    <!-- Main content area - will be replaced by page content -->
    <main th:replace="~{::content}">
        תוכן ברירת מחדל
    </main>
    
    <!-- Footer section -->
    <footer th:replace="~{fragments/footer :: footer}">כותרת תחתונה</footer>
    
    <!-- Common JavaScript -->
    <script th:src="@{/js/main.js}"></script>
    <!-- Placeholder for page-specific scripts -->
    <th:block th:replace="~{::scripts}"></th:block>
</body>
</html>
```

<div dir="rtl">

#### שימוש ב-Layout (home.html)

</div>

```html
<!DOCTYPE html>
<!-- This page will use the layout template -->
<html xmlns:th="http://www.thymeleaf.org" th:replace="~{layout :: html}">
<head>
    <title>דף הבית</title>
    <!-- Page-specific CSS -->
    <th:block th:fragment="head-additional">
        <link rel="stylesheet" th:href="@{/css/home.css}">
    </th:block>
</head>
<body>
    <!-- Page-specific content -->
    <main th:fragment="content">
        <h1>ברוכים הבאים לדף הבית</h1>
        <p th:text="${message}">הודעת ברכה</p>
    </main>
    
    <!-- Page-specific scripts -->
    <th:block th:fragment="scripts">
        <script th:src="@{/js/home.js}"></script>
    </th:block>
</body>
</html>
```

<div dir="rtl">

### טבלאות נתונים מתקדמות

</div>

```html
<!-- Advanced data table example -->
<table>
    <thead>
        <tr>
            <th>מזהה</th>
            <th>שם</th>
            <th>אימייל</th>
            <th>תאריך הצטרפות</th>
            <th>פעולות</th>
        </tr>
    </thead>
    <tbody>
        <!-- Iterate over users with alternating row colors -->
        <tr th:each="user, stat : ${users}" th:class="${stat.odd}? 'odd' : 'even'">
            <td th:text="${user.id}">1</td>
            <td th:text="${user.name}">ישראל ישראלי</td>
            <td th:text="${user.email}">israel@example.com</td>
            <td th:text="${#dates.format(user.joinDate, 'dd/MM/yyyy')}">01/01/2023</td>
            <td>
                <!-- Action buttons with dynamic URLs -->
                <a th:href="@{/user/{id}(id=${user.id})}" class="btn btn-info">צפייה</a>
                <a th:href="@{/user/{id}/edit(id=${user.id})}" class="btn btn-warning">עריכה</a>
                <a th:href="@{/user/{id}/delete(id=${user.id})}" 
                   onclick="return confirm('האם אתה בטוח שברצונך למחוק?');" 
                   class="btn btn-danger">מחיקה</a>
            </td>
        </tr>
        <!-- Empty state message -->
        <tr th:if="${#lists.isEmpty(users)}">
            <td colspan="5">אין משתמשים להצגה</td>
        </tr>
    </tbody>
</table>
```

<div dir="rtl">

### הוספת JavaScript דינמי

</div>

```html
<!-- Inline JavaScript with Thymeleaf integration -->
<script th:inline="javascript">
    /*<![CDATA[*/
    
    // Variables from Thymeleaf are directly injected into JavaScript
    var message = /*[[${message}]]*/ 'ברירת מחדל';
    var userId = /*[[${user.id}]]*/ 0;
    
    // Using the variables
    console.log('הודעה: ' + message);
    
    // Using JSON
    var userObject = /*[[${user}]]*/ {};
    console.log('פרטי משתמש:', userObject);
    
    /*]]>*/
</script>
```

<div dir="rtl">

## דוגמאות מתקדמות מתוך הפרויקט

### דוגמת פרויקט ניהול משתמשים ותפקידים

בהתבסס על קוד הפרויקט, להלן דוגמאות מעשיות לשימוש ב-Thymeleaf לעמודי ניהול משתמשים ותפקידים:

#### מבנה הפרויקט
* **מודלים (Entities)**: `User`, `Role`
* **DTO**: `UserDto`, `RoleDto`, `UserResponseDto`
* **בקרים**: `AdminController`, `LoginController`
* **שירותים**: `UserService`, `RoleService`, `CustomUserDetailsService`
* **תצוגות**: מגוון דפי HTML עם Thymeleaf

#### דוגמת לוח בקרה למנהל (admin-home.html)

</div>

```html
<!-- Header Section with dynamic user information -->
<header>
    <h1>לוח בקרה למנהל</h1>
    <div class="logout-form">
        <a th:href="@{/home}" class="home-button">חזרה לדף הבית</a>
        <button id="logoutBtn" class="action-button">התנתק</button>
    </div>
    <!-- Display authenticated username -->
    <p>ברוך הבא, <span th:text="${#authentication.name}">מנהל</span>!</p>
</header>

<!-- User Management Section -->
<section class="users-section">
    <div class="actions">
        <a th:href="@{/admin/add-user}" class="action-button">הוסף משתמש חדש</a>
    </div>
    <h2>ניהול משתמשים</h2>
    <table>
        <thead>
        <tr>
            <th>שם משתמש</th>
            <th>תפקידים</th>
            <th>פעולות</th>
        </tr>
        </thead>
        <tbody>
        <!-- Iterate through user list -->
        <tr th:each="user : ${users}">
            <td th:text="${user.getUsername()}">שם משתמש</td>
            <td>
                <!-- Display all roles for each user -->
                <span th:each="role : ${user.getRoles()}"
                      th:text="${role.getName()}"
                      class="role-badge">תפקיד</span>
            </td>
            <td>
                <!-- Action buttons with dynamic URLs -->
                <a th:href="@{'/admin/edit-user/' + ${user.getUsername()}}" class="edit-button">ערוך</a>
                <a th:href="@{'/admin/delete-user/' + ${user.getUsername()}}" class="delete-button">מחק</a>
            </td>
        </tr>
        </tbody>
    </table>
</section>
```

<div dir="rtl">

#### דוגמת טופס הוספת משתמש (add-user.html)

</div>

```html
<!-- User Form with model binding -->
<form th:action="@{/admin/add-user}" method="post" th:object="${userDto}">
    <!-- Username Field -->
    <div class="form-group">
        <label for="username">שם משתמש:</label>
        <input type="text" id="username" name="username" th:field="*{username}"
               required minlength="3" maxlength="50">
    </div>

    <!-- Password Field -->
    <div class="form-group">
        <label for="password">סיסמה:</label>
        <input type="password" id="password" name="password" th:field="*{password}"
               required minlength="3" maxlength="50">
    </div>

    <!-- Roles Selection with checkboxes -->
    <div class="form-group">
        <label>תפקידים:</label>
        <div class="checkbox-group">
            <div th:each="role : ${availableRoles}" class="checkbox-item">
                <input type="checkbox" th:id="${'role-' + role.getName()}"
                       name="roles" th:value="${role.getName()}">
                <label th:for="${'role-' + role.getName()}"
                       th:text="${role.getName()}">תפקיד</label>
            </div>
        </div>
    </div>

    <!-- Form Buttons -->
    <div class="button-group">
        <a th:href="@{/admin/dashboard}" class="cancel-button">ביטול</a>
        <button type="submit" class="submit-button">הוסף משתמש</button>
    </div>
</form>
```

<div dir="rtl">

#### דוגמת דף התחברות (login.html)

</div>

```html
<div class="login-container">
    <!-- Login Header -->
    <h2>Login</h2>

    <!-- Login Form with Spring Security integration -->
    <form th:action="@{/login}" method="post">
        <!-- CSRF Protection token -->
        <input type="hidden" name="_csrf" th:value="${_csrf.token}"/>

        <!-- Username Field -->
        <div class="input-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required />
        </div>

        <!-- Password Field -->
        <div class="input-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required />
        </div>

        <!-- Submit Button -->
        <button type="submit" class="login-button">Login</button>

        <!-- Error Message (if authentication failed) -->
        <div th:if="${param.error}">
            <p class="error">Invalid username or password!</p>
        </div>
    </form>
</div>
```

<div dir="rtl">

#### דוגמת בקר ניהול (AdminController.java)

</div>

```java
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    /**
     * הצגת לוח הבקרה למנהל
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Fetch data for dashboard display
        List<User> users = userService.getAllUsers();
        List<Role> roles = roleService.getAllRoles();
        // Add data to the model for template access
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin-home";
    }

    /**
     * הצגת טופס להוספת משתמש
     */
    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        // Prepare empty DTO and available roles for form
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("availableRoles", roleService.getAllRoles());
        return "add-user";
    }

    /**
     * עיבוד הטופס להוספת משתמש
     */
    @PostMapping("/add-user")
    public String addUser(@Valid @ModelAttribute UserDto userDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("availableRoles", roleService.getAllRoles());
            return "add-user";
        }

        // Try to register user and handle business exceptions
        try {
            userService.registerUser(userDto);
            redirectAttributes.addFlashAttribute("success", "המשתמש נוצר בהצלחה");
            return "redirect:/admin/dashboard";
        } catch (InvalidRequestException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("availableRoles", roleService.getAllRoles());
            return "add-user";
        }
    }
}
```

<div dir="rtl">

### עבודה עם הודעות שגיאה והצלחה

#### הצגת הודעות שגיאה

</div>

```html
<!-- Display error message if present in the model -->
<div th:if="${error}" class="error-message" th:text="${error}">
    הודעת שגיאה
</div>
```

<div dir="rtl">

#### שימוש ב-RedirectAttributes להודעות הצלחה

</div>

```java
// Add success message to flash attributes (persists through redirect)
redirectAttributes.addFlashAttribute("success", "המשתמש נוצר בהצלחה");
return "redirect:/admin/dashboard";
```

```html
<!-- Display success message in the template -->
<div class="alert alert-success" th:if="${success}">
    <span th:text="${success}">הפעולה הושלמה בהצלחה</span>
</div>
```

<div dir="rtl">

### עבודה עם אבטחה ו-Spring Security

#### גישה למידע המשתמש המחובר

</div>

```html
<!-- Display authenticated username -->
<p>ברוך הבא, <span th:text="${#authentication.name}">משתמש</span>!</p>

<!-- Display user roles -->
<h3>התפקידים שלך הם:
    <span th:each="auth, iterStat : ${#authentication.principal.authorities}"
          th:text="${#strings.replace(auth, 'ROLE_', '') + (!iterStat.last ? ', ' : '')}">
    </span>
</h3>
```

<div dir="rtl">

#### הצגת תוכן בהתאם להרשאות

</div>

```html
<!-- Show content only to users with ADMIN role -->
<div class="button-container" sec:authorize="hasRole('ADMIN')">
    <a th:href="@{/admin/dashboard}" class="admin-button">לוח בקרה למנהל</a>
</div>
```

<div dir="rtl">

### התמודדות עם שגיאות וחריגים

בפרויקט קיימות מספר דרכים להתמודד עם שגיאות:

#### טיפול בשגיאות בקונטרולר

</div>

```java
@PostMapping("/add-user")
public String addUser(@Valid @ModelAttribute UserDto userDto,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes,
                      Model model) {

    // Handle validation errors
    if (bindingResult.hasErrors()) {
        model.addAttribute("availableRoles", roleService.getAllRoles());
        return "add-user";
    }

    // Handle business logic exceptions
    try {
        userService.registerUser(userDto);
        redirectAttributes.addFlashAttribute("success", "המשתמש נוצר בהצלחה");
        return "redirect:/admin/dashboard";
    } catch (InvalidRequestException e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("availableRoles", roleService.getAllRoles());
        return "add-user";
    }
}
```

<div dir="rtl">

#### שימוש ב-ControllerAdvice לטיפול גלובלי בשגיאות

</div>

```java
@ControllerAdvice(basePackages = "org.example.stage6.controller")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AdminExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public Object handleAdminInvalidRequest(InvalidRequestException ex, 
                                           WebRequest request, 
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        // Handle API requests
        if (isApiRequest(request)) {
            Map<String, String> details = new HashMap<>();
            details.put("type", "Admin Operation Error");
            details.put("message", ex.getMessage());
            
            StandardResponse response = new StandardResponse("error", null, details);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            // Handle browser requests
            if (request.getHeader("Referer") != null) {
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
                return "redirect:" + request.getHeader("Referer");
            } else {
                model.addAttribute("error", ex.getMessage());
                return "error";
            }
        }
    }
}
```

<div dir="rtl">

### אימות נתונים עם Bean Validation

הפרויקט משתמש ב-Bean Validation לאימות נתונים:

</div>

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    // Username validation - required, with length constraints
    @NotBlank(message = "username required")
    @Size(min = 3, max = 50, message = "username must be between 3 and 30 characters")
    private String username;

    // Password validation - required, with length constraints
    @NotBlank(message = "password is required")
    @Size(min = 3, max = 50, message = "password must be between 3 and 30 characters")
    private String password;

    private HashSet<String> roles;
}
```

<div dir="rtl">

הצגת שגיאות ולידציה בטופס:

</div>

```html
<div>
    <label for="username">שם משתמש:</label>
    <input type="text" id="username" th:field="*{username}" />
    <!-- Display validation errors for this field -->
    <span th:if="${#fields.hasErrors('username')}" th:errors="*{username}">שגיאת שם משתמש</span>
</div>
```

<div dir="rtl">

### עבודה עם מודאלים (Modal Dialogs)

</div>

```html
<!-- Button to trigger modal -->
<button id="logoutBtn" class="logout-button">התנתק</button>

<!-- Logout confirmation modal -->
<div id="logoutModal" class="modal">
    <div class="modal-content">
        <h3>אישור התנתקות</h3>
        <p>האם אתה בטוח שברצונך להתנתק?</p>
        <div class="modal-buttons">
            <form th:action="@{/logout}" method="post" id="logoutForm">
                <button type="button" id="cancelBtn" class="cancel-button">ביטול</button>
                <button type="submit" class="confirm-button">כן, התנתק</button>
            </form>
        </div>
    </div>
</div>

<!-- JavaScript to control the modal -->
<script th:src="@{/js/modal.js}" defer></script>
```

<div dir="rtl">

### טיפול בבקשות DELETE בטוחות

אבטחת מחיקת משאבים באמצעות טופס אישור:

</div>

```html
<!-- Deletion confirmation message -->
<p th:if="${type == 'user'}" class="confirmation-message">
    האם אתה בטוח שברצונך למחוק את המשתמש "<span th:text="${id}"></span>"?
</p>

<!-- Deletion form with POST method for better security -->
<form th:if="${type == 'user'}"
      th:action="@{'/admin/delete-user/' + ${id} + '/confirm'}"
      method="post" style="display:inline;">
    <button type="submit" class="delete-button">מחק</button>
</form>
```


```java
// Show deletion confirmation page
@GetMapping("/delete-user/{username}")
public String showDeleteUserConfirmation(@PathVariable String username, Model model) {
    model.addAttribute("id", username);
    model.addAttribute("type", "user");
    return "delete-confirmation";
}

// Process actual deletion after confirmation
@PostMapping("/delete-user/{username}/confirm")
public String deleteUser(@PathVariable String username, RedirectAttributes redirectAttributes) {
    try {
        userService.deleteUser(username);
        redirectAttributes.addFlashAttribute("success", "המשתמש נמחק בהצלחה");
    } catch (InvalidRequestException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/dashboard";
}
```

<div dir="rtl">

## משאבים נוספים ללמידה

1. **תיעוד רשמי**: [אתר Thymeleaf הרשמי](https://www.thymeleaf.org/documentation.html)
2. **מדריכי Spring Boot**: [Spring Boot + Thymeleaf](https://spring.io/guides/gs/serving-web-content/)
3. **קורסים מקוונים**: Udemy, Pluralsight, ו-Baeldung מציעים קורסים מעמיקים על Spring Boot ו-Thymeleaf.
4. **דוגמאות קוד**: [Spring Boot Thymeleaf Examples](https://github.com/spring-guides/gs-serving-web-content)

## תרגילים מעשיים מומלצים

להלן מספר תרגילים מעשיים שיעזרו לך להתמקצע ב-Thymeleaf:

1. **יצירת תבנית בסיסית**:
   * צור דף HTML בסיסי עם Thymeleaf שמציג משתנה מהמודל
   * הוסף תפריט ניווט בסיסי

2. **יצירת מערכת תבניות**:
   * צור תבנית בסיסית (layout) עם כותרת עליונה, תפריט וכותרת תחתונה
   * צור 3 דפים שונים המשתמשים בתבנית זו

3. **טופס עם ולידציה**:
   * צור טופס ליצירת אובייקט עם לפחות 5 שדות
   * הוסף ולידציה והצגת שגיאות
   * הוסף הצגת הודעת הצלחה לאחר שליחת הטופס

4. **טבלת נתונים מתקדמת**:
   * צור טבלה שמציגה רשימת אובייקטים
   * הוסף מיון, סינון ודפדוף
   * הוסף אפשרויות לעריכה ומחיקה

5. **מערכת הרשאות**:
   * צור מערכת עם משתמשים בעלי הרשאות שונות
   * הצג או הסתר תוכן בהתבסס על הרשאות המשתמש

6. **אינטראקציה עם JavaScript**:
   * צור דף שמשלב תוכן דינמי מ-Thymeleaf עם JavaScript
   * הוסף בקשת AJAX שמעדכנת חלק מהדף ללא טעינה מחדש



Thymeleaf הוא מנוע תבניות חזק ואינטואיטיבי שמשתלב היטב עם Spring Boot. כפי שראינו מהדוגמאות בפרויקט, הוא מאפשר:

1. **שילוב פשוט של לוגיקה בתצוגה** - קל להשתמש בתנאים, לולאות, וגישה לנתוני מודל
2. **טיפול בטפסים** - עם קישור ישיר למודלים ותמיכה בולידציה
3. **אינטגרציה עם Spring Security** - גישה קלה למידע אבטחה והצגת תוכן בהתאם להרשאות
4. **מודולריות** - שימוש בפרגמנטים ותבניות לקוד נקי ומודולרי
5. **תמיכה בהתראות ושגיאות** - טיפול אלגנטי בהודעות שגיאה והצלחה
6. **עבודה עם JavaScript ו-AJAX** - שילוב קל של תוכן דינמי

הקפד להשתמש בתכונות כמו פרגמנטים, layouts ו-expressions כדי להפוך את הקוד שלך ליותר מודולרי וקל לתחזוקה. בנוסף, העזר במשאבים הנוספים כדי להעמיק את הידע שלך ולגלות תכונות מתקדמות נוספות.

הדוגמאות שראינו מהפרויקט מדגימות מקרי שימוש אמיתיים, החל מטופסי כניסה ועד לניהול משתמשים מורכב עם אבטחה. שימוש נכון ב-Thymeleaf יכול לסייע לכתוב קוד צד-שרת נקי יותר, עם הפרדה טובה בין שכבת העסקים ושכבת התצוגה, תוך שמירה על קוד HTML תקין שניתן לתצוגה גם ללא עיבוד דינמי.

</div>