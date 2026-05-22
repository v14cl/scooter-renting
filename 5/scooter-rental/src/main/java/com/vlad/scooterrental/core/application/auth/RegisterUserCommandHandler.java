package com.vlad.scooterrental.core.application.auth;

import com.vlad.scooterrental.core.api.event.UserRegisteredEvent;
import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.factory.UserFactory;
import com.vlad.scooterrental.core.domain.model.Role;
import com.vlad.scooterrental.core.domain.repository.PasswordHasher;
import com.vlad.scooterrental.core.domain.repository.UserRepository;
import com.vlad.scooterrental.shared.event.EventPublisher;
import java.time.Instant;
import java.util.UUID;

public class RegisterUserCommandHandler {
  private final UserFactory userFactory;
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final EventPublisher eventPublisher;

  public RegisterUserCommandHandler(
      UserFactory userFactory,
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      EventPublisher eventPublisher) {
    this.userFactory = userFactory;
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.eventPublisher = eventPublisher;
  }

  public UUID handle(RegisterUserCommand command) {
    if (command.password() == null || command.password().isBlank()) {
      throw new ValidationException("Password must not be blank");
    }
    var user =
        userFactory.create(
            command.fullName(),
            command.email(),
            passwordHasher.hash(command.password()),
            Role.CUSTOMER);
    var savedUser = userRepository.save(user);
    eventPublisher.publish(
        new UserRegisteredEvent(
            UUID.randomUUID(),
            savedUser.id(),
            savedUser.email().value(),
            savedUser.fullName(),
            Instant.now()));
    return savedUser.id();
  }
}
