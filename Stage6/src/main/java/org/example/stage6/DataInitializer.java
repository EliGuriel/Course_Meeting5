package org.example.stage6;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stage6.entity.Role;
import org.example.stage6.entity.User;
import org.example.stage6.repository.RoleRepository;
import org.example.stage6.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

/**
 * This class initializes the database with default users and roles if they don't exist.
 * It creates two default users: admin and user, each with their respective roles.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("Starting database initialization...");
            initRoles();
            initUsers();
            log.info("Database initialization completed.");
        };
    }

    private void initRoles() {
        if (roleRepository.findByName("ADMIN") == null) {
            log.info("Creating ADMIN role");
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setUsers(new ArrayList<>());
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("USER") == null) {
            log.info("Creating USER role");
            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setUsers(new ArrayList<>());
            roleRepository.save(userRole);
        }
    }

    private void initUsers() {
        // Create an admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            log.info("Creating admin user");
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setRoles(new ArrayList<>());
            
            Role adminRole = roleRepository.findByName("ADMIN");
            if (adminRole != null) {
                adminUser.getRoles().add(adminRole);
            } else {
                log.error("ADMIN role not found when creating admin user!");
            }
            
            userRepository.save(adminUser);
        }

        // Create a regular user if it doesn't exist
        if (!userRepository.existsByUsername("user")) {
            log.info("Creating regular user");
            User regularUser = new User();
            regularUser.setUsername("user");
            regularUser.setPassword(passwordEncoder.encode("user"));
            regularUser.setRoles(new ArrayList<>());
            
            Role userRole = roleRepository.findByName("USER");
            if (userRole != null) {
                regularUser.getRoles().add(userRole);
            } else {
                log.error("USER role not found when creating regular user!");
            }
            
            userRepository.save(regularUser);
        }
    }
}