package org.example.stage5.config;

import lombok.RequiredArgsConstructor;
import org.example.stage5.service.CustomUserDetailsService;
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

                        // TODO Stage5: allow resources access to authenticated users
                        // Allow access to static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**").authenticated()

                        .requestMatchers("/role", "/register", "/admin_home").hasRole("ADMIN")
                        .requestMatchers("/home").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())

                .formLogin(form -> form

                        /* TODO Stage5: custom login page ************* important *************
                         * This will eliminate the default login page and use our custom login page
                         * The login page is located in src/main/resources/templates/login.html
                         */
                        .loginPage("/login")

                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")

                        // TODO Stage5 Redirect after logout
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        // TODO Stage5: no need to set userDetailsService here, because we are using the default userDetailsService
        // .userDetailsService(userDetailsService); // we can remove this line, because spring security will use the default userDetailsService,
        // and we have only one userDetailsService in our project

        return http.build();
    }
}
