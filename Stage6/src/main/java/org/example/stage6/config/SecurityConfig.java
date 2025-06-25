package org.example.stage6.config;

import lombok.RequiredArgsConstructor;
import org.example.stage6.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /*
    TODO Stage6 - Adding logging to the SecurityConfig class
     */
    Logger logger = Logger.getLogger(SecurityConfig.class.getName());

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        // TODO Stage6 - BCrypt is a strong hashing algorithm that prevents rainbow table attacks
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //  Preventing XSS, Clickjacking, and Setting CSP Headers
        http


                /*
                TODO Stage6 -  Preventing XSS, Clickjacking, and Setting CSP Headers
                 CSP prevents XSS attacks by restricting the sources from which JavaScript/CSS can be loaded
                */
                .headers(headers -> headers
                        .contentSecurityPolicy(policy ->
                                // default-src 'self' - Load resources only from the current domain
                                // script-src 'self' 'unsafe-inline' - JavaScript only from current domain + inline (not recommended for production!)
                                // style-src 'self' 'unsafe-inline' - CSS only from current domain + inline styles
                                policy.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline';"))
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) //  Clickjacking
                )

                /*
                TODO Stage6 - CSRF Protection with Cookie Repository
                use csrf protection, withHttpOnlyFalse, so that JavaScript can access the cookie
                This is required for sending AJAX requests with CSRF token
                 */
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                .authorizeHttpRequests(auth -> auth

                        // Allow access to static resources to authenticated users
                        // TODO Stage6 - Added /static/** for additional static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").authenticated()

                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/role", "/register", "/admin_home").hasRole("ADMIN")
                        .requestMatchers("/home").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")

                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login")
                        // TODO Stage6 - Delete session cookie to prevent session hijacking
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                /*
                 TODO Stage6 - session management, session fixation protection, and maximum sessions, for session hijacking protection
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        // TODO Stage6 - migrateSession creates new session ID after login to prevent Session Fixation attacks
                        .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession) //  Session Fixation attack protection
                        // TODO Stage6 - Limit concurrent sessions per user to prevent session sharing/hijacking
                        .maximumSessions(1) // limit the number of sessions a user can have
                        // TODO Stage6 - Log when session expires for monitoring purposes
                        .expiredSessionStrategy(event -> logger.warning("Session expired for user: " + event.getSessionInformation().getPrincipal())));

        // TODO Stage5: no need to set userDetailsService here, because we are using the default userDetailsService
        // .userDetailsService(userDetailsService); // we can remove this line, because spring security will use the default userDetailsService,
        // and we have only one userDetailsService in our project
        // and we have only one userDetailsService in our project

        return http.build();
    }
}
