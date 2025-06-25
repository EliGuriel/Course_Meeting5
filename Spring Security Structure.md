
<div dir = "rtl">

# מערכת האבטחה של Spring Security - תהליך התחברות וגישה מבוססת תפקידים

 תרשים המתאר את תהליך האבטחה ב-Spring Security, כולל התחברות, קוקי וגישה מבוססת תפקידים:

</div>

```mermaid
flowchart TD
    Client([לקוח HTTP]) --> |"1. שולח בקשת גישה"|ServletContainer[Servlet Container]
    ServletContainer --> DFP[DelegatingFilterProxy]
    DFP --> FCP[FilterChainProxy]
    
    FCP --> SCF[SecurityContextFilter]
    SCF --> |"2. בודק אם קיים SecurityContext"|CookieCheck{"יש קוקי סשן?"}
    
    CookieCheck -->|"לא"|AuthFilter[AuthenticationFilter]
    AuthFilter -->|"3. מפנה"|LoginPage[דף התחברות]
    LoginPage -->|"4. שליחת פרטים"|LoginRequest[בקשת התחברות]
    
    LoginRequest -->|"5. שם משתמש וסיסמה"|UsernamePasswordFilter[UsernamePasswordAuthenticationFilter]
    UsernamePasswordFilter -->|"6. אימות"|AuthManager[AuthenticationManager]
    AuthManager -->|"7. טעינת פרטים"|UserDetailsService[UserDetailsService]
    UserDetailsService -->|"8. פרטי משתמש ותפקידים"|AuthManager
    
    AuthManager -->|"9. אימות מוצלח"|UsernamePasswordFilter
    UsernamePasswordFilter -->|"10. יצירת SecurityContext"|SessionCreation[יצירת סשן וקוקי]
    SessionCreation -->|"11. הפניה עם קוקי"|Client
    
    CookieCheck -->|"כן"|SecurityContext["SecurityContext\n(פרטי המשתמש)"]
    SecurityContext -->|"12. העברת SecurityContext"|AuthorizationFilter[AuthorizationFilter]
    
    AuthorizationFilter -->|"13. בדיקת הרשאות"|RoleCheck{"תפקיד מתאים?"}
    RoleCheck -->|"כן"|ProtectedResource["המשאב המוגן\n(Controller)"]
    RoleCheck -->|"לא"|Forbidden["דף 403\n(Forbidden)"]
    
    ProtectedResource -->|"14. תוכן למשתמש מאושר"|Client
```

<div dir = "rtl">

## הסבר התהליך

1. **בקשה ראשונית**: לקוח שולח בקשת HTTP לגישה למשאב מוגן

2. **בדיקת אימות**:
    - המערכת בודקת אם קיים קוקי סשן תקף
    - אם אין קוקי, המשתמש מופנה לדף התחברות

3. **תהליך התחברות**:
    - המשתמש מזין שם וסיסמה בטופס התחברות
    - `UsernamePasswordAuthenticationFilter` תופס את הבקשה
    - פרטי המשתמש נשלחים ל-`AuthenticationManager`
    - המנהל מבקש מ-`UserDetailsService` את פרטי המשתמש וההרשאות
    - מתבצעת אימות הסיסמה מול הסיסמה המאוחסנת

4. **יצירת סשן**:
    - לאחר אימות מוצלח, נוצר אובייקט `Authentication`
    - מוכנס לתוך `SecurityContext` חדש
    - נוצר קוקי סשן שנשלח ללקוח

5. **בדיקת הרשאות**:
    - בבקשות הבאות, `SecurityContext` נטען מהסשן
    - `AuthorizationFilter` בודק אם התפקידים של המשתמש מאפשרים גישה
    - אם יש הרשאה מתאימה, הבקשה מועברת למשאב
    - אם אין הרשאה, מוחזרת שגיאת 403

תרשים זה ממחיש את הזרימה הבסיסית של מערכת האבטחה ב-Spring Security, כולל תהליך ההתחברות, שמירת והעברת זהות המשתמש, ובדיקות ההרשאה לפי תפקידים.

``` mermaid
sequenceDiagram
    actor Client
    participant Servlet Container
    participant DelegatingFilterProxy
    participant FilterChainProxy
    participant SecurityFilterChain
    box "Spring Security Filters"
        participant ChannelProcessingFilter
        participant WebAsyncManagerIntegrationFilter
        participant SecurityContextPersistenceFilter
        participant HeaderWriterFilter
        participant CsrfFilter
        participant LogoutFilter
        participant UsernamePasswordAuthenticationFilter
        participant RequestCacheFilter
        participant SecurityContextHolderAwareFilter
        participant AnonymousAuthenticationFilter
        participant SessionManagementFilter
        participant ExceptionTranslationFilter
        participant FilterSecurityInterceptor
    end
    participant ProtectedResource
    participant AuthenticationManager
    participant AuthenticationProviders
    participant UserDetailsService

    Client->>Servlet Container: HTTP בקשת
    Servlet Container->>DelegatingFilterProxy: העברת הבקשה
    
    DelegatingFilterProxy->>FilterChainProxy: האצלת עיבוד הבקשה
    
    FilterChainProxy->>FilterChainProxy: בחירת ה-SecurityFilterChain המתאים לפי דפוס ה-URL
    
    FilterChainProxy->>SecurityFilterChain: העברת הבקשה לשרשרת המתאימה
    
    SecurityFilterChain->>ChannelProcessingFilter: עיבוד הבקשה (למשל אכיפת HTTPS)
    
    ChannelProcessingFilter->>WebAsyncManagerIntegrationFilter: המשך שרשרת
    
    WebAsyncManagerIntegrationFilter->>SecurityContextPersistenceFilter: המשך שרשרת
    
    SecurityContextPersistenceFilter->>SecurityContextPersistenceFilter: שחזור ה-SecurityContext מהסשן
    
    SecurityContextPersistenceFilter->>HeaderWriterFilter: המשך שרשרת
    
    HeaderWriterFilter->>CsrfFilter: המשך שרשרת
    
    CsrfFilter->>CsrfFilter: וידוא תקינות טוקן CSRF
    
    CsrfFilter->>LogoutFilter: המשך שרשרת
    
    LogoutFilter->>LogoutFilter: בדיקה אם זו בקשת התנתקות
    
    alt בקשת התנתקות
        LogoutFilter-->>Client: הפניה לדף התנתקות
    else לא בקשת התנתקות
        LogoutFilter->>UsernamePasswordAuthenticationFilter: המשך שרשרת
    end
    
    UsernamePasswordAuthenticationFilter->>UsernamePasswordAuthenticationFilter: בדיקה אם זו בקשת התחברות (login)
    
    alt בקשת התחברות
        UsernamePasswordAuthenticationFilter->>AuthenticationManager: שליחה לאימות
        AuthenticationManager->>AuthenticationProviders: בדיקת אימות
        AuthenticationProviders->>UserDetailsService: טעינת פרטי משתמש
        UserDetailsService-->>AuthenticationProviders: החזרת פרטי משתמש
        AuthenticationProviders-->>AuthenticationManager: תוצאת האימות
        AuthenticationManager-->>UsernamePasswordAuthenticationFilter: תוצאת האימות
        
        alt אימות הצליח
            UsernamePasswordAuthenticationFilter->>SecurityContextPersistenceFilter: שמירת האימות בהקשר האבטחה
            SecurityContextPersistenceFilter-->>Client: הפניה לדף הצלחה
        else אימות נכשל
            UsernamePasswordAuthenticationFilter-->>Client: הפניה לדף כישלון
        end
    else לא בקשת התחברות
        UsernamePasswordAuthenticationFilter->>RequestCacheFilter: המשך שרשרת
    end
    
    RequestCacheFilter->>SecurityContextHolderAwareFilter: המשך שרשרת
    SecurityContextHolderAwareFilter->>AnonymousAuthenticationFilter: המשך שרשרת
    
    AnonymousAuthenticationFilter->>AnonymousAuthenticationFilter: הגדרת אימות אנונימי אם אין אימות קיים
    
    AnonymousAuthenticationFilter->>SessionManagementFilter: המשך שרשרת
    
    SessionManagementFilter->>SessionManagementFilter: בקרת סשן
    
    SessionManagementFilter->>ExceptionTranslationFilter: המשך שרשרת
    
    ExceptionTranslationFilter->>FilterSecurityInterceptor: המשך שרשרת
    
    FilterSecurityInterceptor->>FilterSecurityInterceptor: בדיקת הרשאות לגישה למשאב
    
    alt יש הרשאות מתאימות
        FilterSecurityInterceptor->>ProtectedResource: ביצוע הבקשה למשאב המוגן
        ProtectedResource-->>Client: תשובת HTTP
    else אין הרשאות
        FilterSecurityInterceptor->>ExceptionTranslationFilter: זריקת חריגת אבטחה
        
        alt משתמש לא מזוהה
            ExceptionTranslationFilter-->>Client: הפניה לדף התחברות (401)
        else משתמש מזוהה ללא הרשאות
            ExceptionTranslationFilter-->>Client: החזרת שגיאת הרשאות (403)
        end
    end
```