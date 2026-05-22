package com.vlad.scooterrental.core.application.user.command;

public record CreateUserCommand(String fullName, String email, String password, String role) {}
