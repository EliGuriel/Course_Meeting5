package org.example.stage4.config;

import lombok.RequiredArgsConstructor;
import org.example.stage4.service.CustomUserDetailsService;
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
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/role", "/register", "/admin_home").hasRole("ADMIN") //  ROLE_ADMIN
                        .requestMatchers("/home").hasAnyRole("USER", "ADMIN") //  ROLE_USER or ROLE_ADMIN
                        .anyRequest().authenticated())

                // login processing, logout processing, session management
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll())

                // logout processing
                .logout(logout -> logout
                        .logoutUrl("/logout")

                        // go to GET mapping of /login, not the POST mapping of /login
                        .logoutSuccessUrl("/login?logout=true")

                        // delete the session
                        .deleteCookies("JSESSIONID")
                        .permitAll())

                // session management
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .userDetailsService(userDetailsService);

        return http.build();
    }
}
