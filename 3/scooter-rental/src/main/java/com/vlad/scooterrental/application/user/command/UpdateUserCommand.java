package com.vlad.scooterrental.application.user.command;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.model.Role;

import java.util.UUID;

public record UpdateUserCommand(UUID userId, String fullName, String email, String password, Role role) {

    public static UpdateUserCommand of(
            UUID userId,
            String fullName,
            String email,
            String password,
            String roleName
    ) {
        if (roleName == null || roleName.isBlank()) {
            throw new ValidationException("Role must be provided");
        }
        try {
            return new UpdateUserCommand(
                    userId,
                    fullName,
                    email,
                    password,
                    Role.valueOf(roleName.trim().toUpperCase())
            );
        } catch (IllegalArgumentException exception) {
            throw new ValidationException("Unsupported role: " + roleName);
        }
    }
}
