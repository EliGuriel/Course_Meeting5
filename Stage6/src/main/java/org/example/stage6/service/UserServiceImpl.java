package org.example.stage6.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.stage6.dto.UserDto;
import org.example.stage6.dto.UserResponseDto;
import org.example.stage6.entity.Role;
import org.example.stage6.entity.User;
import org.example.stage6.exception.InvalidRequestException;
import org.example.stage6.repository.RoleRepository;
import org.example.stage6.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new InvalidRequestException("User not found: " + username));
    }

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

        return createUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getUsername())
                .orElseThrow(() -> new InvalidRequestException("User not found: " + userDto.getUsername()));

        // Update password if provided
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        // Update roles
        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            user.getRoles().clear();

            for (String role : userDto.getRoles()) {
                Role roleFound = roleRepository.findByName(role.toUpperCase());
                if (roleFound != null) {
                    user.getRoles().add(roleFound);
                }
            }

            if (user.getRoles().isEmpty()) {
                throw new InvalidRequestException("No valid roles found for user: " + userDto.getUsername());
            }
        }

        userRepository.save(user);

        return createUserResponseDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new InvalidRequestException("User not found: " + username));

        userRepository.delete(user);
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "Anonymous";
    }

    private UserResponseDto createUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toCollection(HashSet::new)));

        return userResponseDto;
    }
}