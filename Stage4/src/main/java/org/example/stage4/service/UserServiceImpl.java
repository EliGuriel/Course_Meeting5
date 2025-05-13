package org.example.stage4.service;

import lombok.RequiredArgsConstructor;
import org.example.stage4.dto.UserDto;
import org.example.stage4.dto.UserResponseDto;
import org.example.stage4.entity.Role;
import org.example.stage4.entity.User;
import org.example.stage4.exception.InvalidRequestException;
import org.example.stage4.repository.RoleRepository;
import org.example.stage4.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto registerUser(UserDto userDto) {
        // check if the user already exists
        if (userRepository.existsById(userDto.getUsername())) {
            throw new InvalidRequestException("User already exists: " + userDto.getUsername());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        for (String role : userDto.getRoles()) {
            Role roleFound = roleRepository.findByName(role.toUpperCase());
            if (roleFound != null) {
                user.getRoles().add(roleFound);
            }
        }

        if (user.getRoles().isEmpty()) {
            throw new InvalidRequestException("No roles found for user: " + userDto.getUsername());
        }

        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toCollection(HashSet::new)));

        return userResponseDto;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "Anonymous";
    }
}