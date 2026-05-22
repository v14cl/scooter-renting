package com.vlad.scooterrental.core.application.user.command;

import com.vlad.scooterrental.core.domain.exception.NotFoundException;
import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.factory.UserFactory;
import com.vlad.scooterrental.core.domain.model.Role;
import com.vlad.scooterrental.core.domain.repository.PasswordHasher;
import com.vlad.scooterrental.core.domain.repository.UserRepository;

public class UpdateUserCommandHandler {
  private final UserFactory userFactory;
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;

  public UpdateUserCommandHandler(
      UserFactory userFactory, UserRepository userRepository, PasswordHasher passwordHasher) {
    this.userFactory = userFactory;
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
  }

  public void handle(UpdateUserCommand command) {
    var existingUser =
        userRepository
            .findById(command.userId())
            .orElseThrow(() -> new NotFoundException("User not found"));
    String passwordHash = existingUser.passwordHash();
    if (command.password() != null && !command.password().isBlank()) {
      passwordHash = passwordHasher.hash(command.password());
    } else if (passwordHash == null || passwordHash.isBlank()) {
      throw new ValidationException("Password must not be blank");
    }
    var updatedUser =
        userFactory.update(
            command.userId(), command.fullName(), command.email(), passwordHash, parseRole(command.role()));
    userRepository.save(updatedUser);
  }

  private Role parseRole(String roleName) {
    if (roleName == null || roleName.isBlank()) {
      throw new ValidationException("Role must be provided");
    }
    try {
      return Role.valueOf(roleName.trim().toUpperCase());
    } catch (IllegalArgumentException exception) {
      throw new ValidationException("Unsupported role: " + roleName);
    }
  }
}
