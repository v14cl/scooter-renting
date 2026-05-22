package com.vlad.scooterrental.application.user.command;

public record CreateUserCommand(String fullName, String email, String password, String role) {}
