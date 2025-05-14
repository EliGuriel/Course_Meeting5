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
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //  Preventing XSS, Clickjacking, and Setting CSP Headers
        http


                /*
                TODO Stage6 -  Preventing XSS, Clickjacking, and Setting CSP Headers
                 */
                .headers(headers -> headers
                        .contentSecurityPolicy(policy ->
                                policy.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline';"))
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) //  Clickjacking
                )

                /*
                use csrf protection, withHttpOnlyFalse, so that JavaScript can access the cookie
                 */
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                .authorizeHttpRequests(auth -> auth

                        // Allow access to static resources to authenticated users
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
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                /*
                 TODO Stage6 - session management, session fixation protection, and maximum sessions, for session hijacking protection
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession) //  Session Fixation attack protection
                        .maximumSessions(1) // limit the number of sessions a user can have
                        .expiredSessionStrategy(event -> logger.warning("Session expired for user: " + event.getSessionInformation().getPrincipal())));
        // TODO Stage5: no need to set userDetailsService here, because we are using the default userDetailsService
        // .userDetailsService(userDetailsService); // we can remove this line, because spring security will use the default userDetailsService,
        // and we have only one userDetailsService in our project

        return http.build();
    }
}
