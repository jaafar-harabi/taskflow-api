package com.example.taskflow.user.dto;

import com.example.taskflow.user.UserRole;

public record AuthResponse(
        String token,
        String email,
        String fullName,
        UserRole role
) {}
