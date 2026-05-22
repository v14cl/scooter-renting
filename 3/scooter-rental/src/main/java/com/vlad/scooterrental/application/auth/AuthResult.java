package com.vlad.scooterrental.application.auth;

public record AuthResult(String token, UserView user) {
}
