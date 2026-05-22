package com.vlad.scooterrental.core.domain.factory;

import com.vlad.scooterrental.core.domain.exception.ConflictException;
import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.model.Role;
import com.vlad.scooterrental.core.domain.model.User;
import com.vlad.scooterrental.core.domain.repository.UserRepository;
import com.vlad.scooterrental.core.domain.value.Email;
import java.util.UUID;

public class UserFactory {

  private final UserRepository userRepository;

  public UserFactory(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User create(String fullName, String email, String passwordHash, Role role) {
    String normalizedName = requireText(fullName, "Full name must not be blank");
    String normalizedHash = requireText(passwordHash, "Password hash must not be blank");
    Email normalizedEmail = Email.of(email);

    if (userRepository.existsByEmail(normalizedEmail)) {
      throw new ConflictException("User with this email already exists");
    }

    return new User(UUID.randomUUID(), normalizedName, normalizedEmail, normalizedHash, role);
  }

  public User update(UUID userId, String fullName, String email, String passwordHash, Role role) {
    String normalizedName = requireText(fullName, "Full name must not be blank");
    String normalizedHash = requireText(passwordHash, "Password hash must not be blank");
    Email normalizedEmail = Email.of(email);

    if (userRepository.existsByEmailAndIdNot(normalizedEmail, userId)) {
      throw new ConflictException("User with this email already exists");
    }

    return new User(userId, normalizedName, normalizedEmail, normalizedHash, role);
  }

  private String requireText(String value, String message) {
    if (value == null || value.isBlank()) {
      throw new ValidationException(message);
    }
    return value.trim();
  }
}
