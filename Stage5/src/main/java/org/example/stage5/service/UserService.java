package org.example.stage5.service;

import org.example.stage5.dto.UserDto;
import org.example.stage5.dto.UserResponseDto;
import org.example.stage5.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
    String getCurrentUsername();

    List<User> getAllUsers();
}