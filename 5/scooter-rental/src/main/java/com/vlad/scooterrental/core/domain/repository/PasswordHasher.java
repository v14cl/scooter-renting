package com.vlad.scooterrental.core.domain.repository;

public interface PasswordHasher {

  String hash(String rawPassword);

  boolean matches(String rawPassword, String passwordHash);
}
