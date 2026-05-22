package com.vlad.scooterrental.core.application.auth;

public record RegisterUserCommand(String fullName, String email, String password) {}
