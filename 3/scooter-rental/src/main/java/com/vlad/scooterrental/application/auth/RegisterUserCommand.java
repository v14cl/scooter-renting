package com.vlad.scooterrental.application.auth;

public record RegisterUserCommand(String fullName, String email, String password) {
}
