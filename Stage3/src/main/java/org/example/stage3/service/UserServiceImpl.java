package org.example.stage3.service;

import lombok.RequiredArgsConstructor;
import org.example.stage3.dto.UserDto;
import org.example.stage3.dto.UserResponseDto;
import org.example.stage3.entity.Role;
import org.example.stage3.entity.User;
import org.example.stage3.exception.InvalidRequestException;
import org.example.stage3.repository.RoleRepository;
import org.example.stage3.repository.UserRepository;
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
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new InvalidRequestException("User already exists: " + userDto.getUsername());
        }

        // create a new user object and set its properties
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // for each role in the userDto, find the role in the roleRepository and add it to the user's roles
       for (String role : userDto.getRoles()) {
            Role roleFound = roleRepository.findByName(role.toUpperCase());
            if (roleFound != null) {
                user.getRoles().add(roleFound);
            }
        }

        // if no role is found, throw an exception
        if (user.getRoles().isEmpty()) {
            throw new InvalidRequestException("No roles found for user: " + userDto.getUsername());
        }

        // save the user to the database
        userRepository.save(user);

        // create a UserResponseDto object to return
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toCollection(HashSet::new)));

        return userResponseDto;
    }

    public String getCurrentUsername() {
        // This method should return the username of the currently authenticated user
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "Anonymous";
    }
}
