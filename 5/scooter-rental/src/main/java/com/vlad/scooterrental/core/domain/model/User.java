package com.vlad.scooterrental.core.domain.model;

import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.value.Email;
import java.util.UUID;

public record User(UUID id, String fullName, Email email, String passwordHash, Role role) {
  public User {
    if (id == null) {
      throw new ValidationException("User id must be provided");
    }
    if (fullName == null || fullName.isBlank()) {
      throw new ValidationException("Full name must not be blank");
    }
    if (email == null) {
      throw new ValidationException("Email must be provided");
    }
    if (passwordHash == null || passwordHash.isBlank()) {
      throw new ValidationException("Password hash must not be blank");
    }
    if (role == null) {
      throw new ValidationException("Role must be provided");
    }
    fullName = fullName.trim();
  }

  public boolean isAdmin() {
    return role == Role.ADMIN;
  }
}
