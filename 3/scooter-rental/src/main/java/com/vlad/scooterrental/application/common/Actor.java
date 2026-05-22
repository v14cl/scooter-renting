package com.vlad.scooterrental.application.common;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.model.Role;

import java.util.UUID;

public record Actor(UUID userId, Role role) {

    public static Actor fromClaims(UUID userId, String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new ValidationException("Role must be provided");
        }
        try {
            return new Actor(userId, Role.valueOf(roleName.trim().toUpperCase()));
        } catch (IllegalArgumentException exception) {
            throw new ValidationException("Unsupported role: " + roleName);
        }
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}
