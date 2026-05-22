package com.vlad.scooterrental.domain.model;

import com.vlad.scooterrental.domain.value.Email;

import java.util.UUID;

public record User(
        UUID id,
        String fullName,
        Email email,
        String passwordHash,
        Role role
) {
}
