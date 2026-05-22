package com.vlad.scooterrental.domain.repository;

public interface PasswordHasher {

  String hash(String rawPassword);

  boolean matches(String rawPassword, String passwordHash);
}
