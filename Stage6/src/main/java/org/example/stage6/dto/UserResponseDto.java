package org.example.stage6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {


    @NotBlank(message = "username required")
    @Size(min = 3, max = 50, message = "username must be between 3 and 30 characters")
    private String username;

    private HashSet<String> roles;
}
