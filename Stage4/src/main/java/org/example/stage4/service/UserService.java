package org.example.stage4.service;

import org.example.stage4.dto.UserDto;
import org.example.stage4.dto.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
    String getCurrentUsername();
}