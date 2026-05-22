package com.vlad.scooterrental.core.application.user.command;

import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.factory.UserFactory;
import com.vlad.scooterrental.core.domain.model.Role;
import com.vlad.scooterrental.core.domain.repository.PasswordHasher;
import com.vlad.scooterrental.core.domain.repository.UserRepository;
import java.util.UUID;

public class CreateUserCommandHandler {
  private final UserFactory userFactory;
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;

  public CreateUserCommandHandler(
      UserFactory userFactory, UserRepository userRepository, PasswordHasher passwordHasher) {
    this.userFactory = userFactory;
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
  }

  public UUID handle(CreateUserCommand command) {
    if (command.password() == null || command.password().isBlank()) {
      throw new ValidationException("Password must not be blank");
    }
    var user =
        userFactory.create(
            command.fullName(),
            command.email(),
            passwordHasher.hash(command.password()),
            parseRole(command.role()));
    return userRepository.save(user).id();
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
