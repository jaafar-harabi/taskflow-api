package com.example.taskflow.user.dto;

import com.example.taskflow.user.UserRole;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank String fullName,
        @Size(min = 6) String password,
        UserRole role
) {}
