package org.example.stage3.config;

import lombok.RequiredArgsConstructor;
import org.example.stage3.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                /*
                In this Stage disable the CSRF protection, because we are not using it in this stage.
                */
                .csrf(AbstractHttpConfigurer::disable)

                /*
                authorization rules
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("role", "/register").hasRole("ADMIN")
                        .anyRequest().authenticated())

                /*
                Session management
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // this line is not needed because spring security will use the default userDetailsService, but it's good to have it here for clarity
                .userDetailsService(userDetailsService)

                /*
                login processing, logout processing, session management
                 */
                .formLogin(form -> form
                        .loginProcessingUrl("/login")       // Endpoint for login requests
                        .defaultSuccessUrl("/home", true)   // Redirect on successful login
                        .failureUrl("/login?error=true")    // Redirect on failure
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/hello")
                        .deleteCookies("JSESSIONID")
                        .permitAll());

        return http.build();
    }
}
