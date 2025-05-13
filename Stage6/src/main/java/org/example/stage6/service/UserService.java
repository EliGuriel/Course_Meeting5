package org.example.stage6.service;

import org.example.stage6.dto.UserDto;
import org.example.stage6.dto.UserResponseDto;
import org.example.stage6.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
    UserResponseDto updateUser(UserDto userDto);
    void deleteUser(String username);
    User getUserByUsername(String username);
    String getCurrentUsername();
    List<User> getAllUsers();
}