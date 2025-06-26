# אינדקס מושגים - Cookies ואבטחה ב-Spring Security

<div dir="rtl">

## מושגי Cookie בסיסיים

**Cookie** - קובץ טקסט קטן שהשרת שולח לדפדפן לשמירת מידע על הסשן

**JSESSIONID** - Cookie מיוחד המכיל מזהה ייחודי של הסשן (לא מכיל תפקידים!)

**Session ID** - מזהה ייחודי שמחבר בין הדפדפן לנתונים המאוחסנים בשרת

**Session Store** - מאגר בשרת (זיכרון/מסד נתונים) שמחזיק את נתוני הסשן

## מרכיבי אבטחה

**Spring Security** - מסגרת אבטחה לאפליקציות Java/Spring Boot

**SecurityContext** - אובייקט המכיל את כל המידע על המשתמש המחובר

**Authentication** - תהליך אימות זהות המשתמש (login)

**Authorization** - בדיקת הרשאות לביצוע פעולות מסוימות

**Principal** - זהות המשתמש (בדרך כלל שם המשתמש)

**Authorities** - רשימת התפקידים/הרשאות של המשתמש

**ROLE** - תפקיד ספציפי כמו ROLE_ADMIN, ROLE_USER

## מרכיבים טכניים

**Security Filter** - פילטר שבודק את כל הבקשות HTTP ומאמת משתמשים

**UserDetails** - אובייקט המכיל מידע על המשתמש (שם, סיסמה, תפקידים)

**UserDetailsService** - שירות שטוען נתוני משתמש ממסד הנתונים

**GrantedAuthority** - הרשאה או תפקיד בודד של המשתמש

**HttpSession** - סשן HTTP שמכיל נתונים בין בקשות

## הגדרות Cookie

**HttpOnly** - דגל שמונע גישה לcookie דרך JavaScript (מונע XSS)

**Secure** - דגל שמאפשר שליחת cookie רק דרך HTTPS

**SameSite** - דגל להגנה מפני CSRF (Lax/Strict/None)

**Path** - נתיב באתר שבו הcookie תקף

**Max-Age/Expires** - זמן תפוגה של הcookie

## ניהול Session

**SessionCreationPolicy** - מדיניות יצירת session (ALWAYS/IF_REQUIRED/NEVER/STATELESS)

**Session Fixation** - התקפה שבה תוקף "קובע" session ID מסוים

**Session Hijacking** - גניבת session ID לצורך התחזות

**Session Timeout** - זמן אחרי שהsession פוג

## תהליכי אבטחה

**Login** - תהליך התחברות משתמש

**Logout** - תהליך התנתקות ומחיקת session

**Remember Me** - מנגנון לזכירת משתמש בין סשנים

**CSRF Protection** - הגנה מפני Cross-Site Request Forgery

**XSS Protection** - הגנה מפני Cross-Site Scripting

## התקפות ואיומים

**CSRF (Cross-Site Request Forgery)** - התקפה שבה אתר זדוני שולח בקשות בשם המשתמש

**XSS (Cross-Site Scripting)** - החדרת קוד JavaScript זדוני לאתר

**Session Hijacking** - גניבת מזהה session לצורך התחזות

**Man-in-the-Middle** - יירוט תקשורת בין דפדפן לשרת

**SQL Injection** - החדרת קוד SQL זדוני דרך קלט משתמש

## קונפיגורציה

**SecurityConfig** - קובץ הגדרות אבטחה ראשי

**@EnableWebSecurity** - אנוטציה להפעלת Spring Security

**@PreAuthorize** - בדיקת הרשאות לפני ביצוע מתודה

**@Secured** - הגבלת גישה למתודה לפי תפקיד

**permitAll()** - אפשר גישה לכולם

**hasRole()** - בדוק אם יש תפקיד מסוים

## מסדי נתונים ואחסון

**In-Memory Storage** - שמירת sessions בזיכרון השרת

**Database Storage** - שמירת sessions במסד נתונים

**Redis Storage** - שמירת sessions במטמון Redis

**Clustered Sessions** - שיתוף sessions בין מספר שרתים

## Headers ו-HTTP

**Set-Cookie Header** - כותרת HTTP לשליחת cookie לדפדפן

**Cookie Header** - כותרת HTTP ששולח הדפדפן עם cookie

**Authorization Header** - כותרת לשליחת אסימוני הרשאה

**Location Header** - כותרת להפניה (redirect)

## קלאסים ואינטרפייסים

**CustomUserDetailsService** - קלאס מותאם אישית לטעינת נתוני משתמש

**User Entity** - אובייקט המייצג משתמש במסד הנתונים

**UserRepository** - אינטרפייס לגישה למסד נתונים של משתמשים

**SimpleGrantedAuthority** - מימוש פשוט של GrantedAuthority

**Collection<GrantedAuthority>** - אוסף של הרשאות משתמש

**HttpServletRequest** - אובייקט בקשת HTTP

**ResponseEntity** - אובייקט תגובת HTTP עם status ו-headers

**LogoutFilter** - פילטר שמטפל בהתנתקות

**SecurityFilterChain** - שרשרת פילטרים של Spring Security

**ConcurrentHashMap** - מבנה נתונים thread-safe לאחסון sessions

## אנוטציות

**@GetMapping** - אנוטציה למיפוי GET requests

**@EventListener** - אנוטציה להאזנה לאירועים

**@EnableWebSecurity** - הפעלת Spring Security

**@PreAuthorize** - בדיקת הרשאות לפני ביצוע מתודה

**@Secured** - הגבלת גישה למתודה לפי תפקיד

## אירועים (Events)

**HttpSessionCreatedEvent** - אירוע יצירת session חדש

**HttpSessionDestroyedEvent** - אירוע הרס session

## כלי debugging ובדיקה

**Browser DevTools** - כלי פיתוח בדפדפן

**Network Tab** - כרטיסיה לבדיקת בקשות HTTP

**Application Tab** - כרטיסיה לבדיקת cookies ו-storage

**Console Tab** - כרטיסיה לbdebugging ו-JavaScript

**F12** - קיצור דרך לפתיחת DevTools

## מיקומי אחסון

**LocalAppData** - תיקיית נתונים מקומית בWindows

**AppData** - תיקיית נתוני אפליקציות בWindows

**SQLite** - מסד נתונים קליל לאחסון cookies

**Browser Cookie Storage** - מיקום אחסון cookies בדפדפן

## פרוטוקולים ותקנים

**RFC 3339** - תקן לפורמט תאריך ושעה

**UTC** - זמן אוניברסלי מתואם

**HTTP** - פרוטוקול העברת היפר-טקסט

**HTTPS** - HTTP מוצפן

## ארכיטקטורה ותשתית

**Load Balancer** - מאזן עומסים בין שרתים

**Single Server** - שרת יחיד

**Multiple Servers** - מספר שרתים בcluster

**Shared Session Store** - מאגר session משותף

**Clustering** - קיבוץ שרתים

**Hazelcast** - פלטפורמת in-memory data grid

## הגדרות אבטחה מתקדמות

**HSTS (HTTP Strict Transport Security)** - כפיית חיבור HTTPS

**Frame Options** - הגנה מפני clickjacking

**Content Type Options** - מניעת MIME type sniffing

**Session Fixation Protection** - הגנה מפני קביעת session ID

**Session Registry** - רישום sessions פעילים

**maximumSessions()** - מגבלת sessions למשתמש

**maxSessionsPreventsLogin()** - מניעת login נוסף

## מתודות וקונפיגורציה

**permitAll()** - אפשר גישה לכולם

**hasRole()** - בדוק תפקיד

**withDefaults()** - הגדרות ברירת מחדל

**sameOrigin()** - הגדרת frame options

**deleteCookies()** - מחיקת cookies בlogout

**invalidateSession()** - ביטול session

## מושגי רשת ואבטחה

**Man-in-the-Middle** - יירוט תקשורת

**WiFi ציבורי** - רשת אלחוטית ציבורית לא מאובטחת

**Navigation** - ניווט באתר

**GET Requests** - בקשות קריאה

**POST Requests** - בקשות שינוי נתונים

## התקפות אבטחה

**XSS (Cross-Site Scripting)** - החדרת קוד JavaScript זדוני שרץ בדפדפן הקורבן

**CSRF (Cross-Site Request Forgery)** - אילוץ משתמש מחובר לבצע פעולות לא רצויות

**Clickjacking** - הטעיית משתמש ללחוץ על אלמנטים חבויים

**Session Fixation** - התקפה שבה תוקף "קובע" session ID מסוים למשתמש

**Session Hijacking** - גניבת session ID לצורך התחזות

**Man-in-the-Middle** - יירוט תקשורת בין דפדפן לשרת

**SQL Injection** - החדרת קוד SQL זדוני דרך קלט משתמש

**MIME Type Sniffing** - ניסיון דפדפן לנחש סוג קובץ (יכול להוביל לXSS)

## הגנות אבטחה מתקדמות

**CSP (Content Security Policy)** - מדיניות המגדירה מאיפה מותר לטעון משאבים

**HSTS (HTTP Strict Transport Security)** - כפיית שימוש בHTTPS בלבד

**X-Frame-Options** - הגנה מפני הטמעה ב-iframe מדומיינים אחרים

**X-Content-Type-Options** - מניעת MIME type sniffing

**X-XSS-Protection** - הגנה נוספת מפני XSS (legacy)

**CSRF Token** - אסימון ייחודי למניעת CSRF attacks

**Session Registry** - מעקב אחר sessions פעילים במערכת

**Remember Me** - מנגנון לזכירת משתמש בין סשנים

## CSP Directives

**default-src** - מקור ברירת מחדל לכל המשאבים

**script-src** - מקור מותר לקבצי JavaScript

**style-src** - מקור מותר לקבצי CSS

**img-src** - מקור מותר לתמונות

**font-src** - מקור מותר לפונטים

**connect-src** - מקור מותר לבקשות Ajax/fetch

**unsafe-inline** - אישור לקוד inline (scripts/styles)

**data URLs** - כתובות המתחילות ב-data: (לתמונות מוטמעות)

## אלמנטים טכניים

**iframe** - אלמנט HTML להטמעת דף בתוך דף

**inline scripts** - קוד JavaScript שנכתב ישירות ב-HTML

**inline styles** - קוד CSS שנכתב ישירות ב-HTML

**Static Resources** - קבצים סטטיים כמו CSS, JS, תמונות

**Console Error** - שגיאה המוצגת ב-console של הדפדפן

**Production-ready** - מוכן לסביבת ייצור

## קלאסים ומתודות מתקדמות

**CookieCsrfTokenRepository** - מחלקה לניהול CSRF tokens בcookies

**withHttpOnlyFalse()** - אפשר גישה לcookie דרך JavaScript

**SessionRegistryImpl** - מימוש סטנדרטי של SessionRegistry

**HeadersConfigurer** - מחלקה להגדרת security headers

**FrameOptionsConfig** - הגדרות X-Frame-Options

**SessionManagementConfigurer** - הגדרות ניהול sessions

**SessionFixationConfigurer** - הגדרות הגנה מSession Fixation

## מתודות הגדרה

**maximumSessions()** - מגבלת מספר sessions למשתמש

**maxSessionsPreventsLogin()** - מניעת login נוסף או kick out ישן

**sessionFixation()** - הגדרת הגנה מSession Fixation

**migrateSession()** - יצירת session ID חדש בעת login

**expiredSessionStrategy()** - אסטרטגיה לטיפול ב-sessions פגי תוקף

**invalidSessionUrl()** - URL להפניה כשsession פג תוקף

**tokenValiditySeconds()** - משך תוקף אסימון Remember Me

**policyDirectives()** - הגדרת CSP directives

**includeSubdomains()** - הכללת subdomains בHSTS

**maxAgeInSeconds()** - משך זמן לזכירת הגדרת HSTS

**sameOrigin()** - הגדרת X-Frame-Options לdomain זהה

**withDefaults()** - שימוש בהגדרות ברירת מחדל

**nosniff** - ערך עבור X-Content-Type-Options

## Session Management מתקדם

**Session Migration** - העברת נתוני session ל-ID חדש

**Concurrent Session Control** - בקרת sessions מקבילים

**Session Expiration Strategy** - אסטרטגיה לטיפול ב-sessions פגי תוקף

**Session Timeout** - זמן אחרי שהsession פוג

**Session Invalidation** - ביטול session

## מושגים נוספים

**Stateless** - אפליקציה שלא שומרת מצב בין בקשות (JWT)

**Stateful** - אפליקציה השומרת מצב בשרת (Session-based)

**JWT (JSON Web Token)** - אסימון אבטחה המכיל מידע מוצפן

**OAuth** - פרוטוקול להרשאה מבוסס tokens

**Basic Authentication** - אימות פשוט עם שם משתמש וסיסמה

**Thread-Safe** - בטוח לשימוש במספר threads

**In-Memory Data Grid** - רשת נתונים בזיכרון

**MIME Type** - סוג תוכן קובץ

**Clickjacking** - התקפת הטעייה בלחיצות

</div>