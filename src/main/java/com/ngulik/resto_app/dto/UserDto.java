package com.ngulik.resto_app.dto;

import com.ngulik.resto_app.enums.UserRole;
import com.ngulik.resto_app.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private UserRole role;

    private UserStatus status;
}
