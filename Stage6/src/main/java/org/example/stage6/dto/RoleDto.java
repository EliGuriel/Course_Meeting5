package org.example.stage6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    @NotBlank(message = "role name required")
    @Size(min = 2, max = 50, message = "role name must be between 2 and 30 characters")
    private String roleName;
}
