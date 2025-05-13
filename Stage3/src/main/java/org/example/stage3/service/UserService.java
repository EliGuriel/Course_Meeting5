package org.example.stage3.service;

import org.example.stage3.dto.UserDto;
import org.example.stage3.dto.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);

    public String getCurrentUsername();
}
