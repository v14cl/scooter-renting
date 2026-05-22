package com.vlad.scooterrental.core.application.user.command;

import java.util.UUID;

public record UpdateUserCommand(
    UUID userId, String fullName, String email, String password, String role) {}
