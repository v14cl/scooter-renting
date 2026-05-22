package com.vlad.scooterrental.application.user.command;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.model.Role;

public record CreateUserCommand(String fullName, String email, String password, Role role) {
  public static CreateUserCommand of(
      String fullName, String email, String password, String roleName) {
    if (roleName == null || roleName.isBlank()) {
      throw new ValidationException("Role must be provided");
    }
    try {
      return new CreateUserCommand(
          fullName, email, password, Role.valueOf(roleName.trim().toUpperCase()));
    } catch (IllegalArgumentException exception) {
      throw new ValidationException("Unsupported role: " + roleName);
    }
  }
}
