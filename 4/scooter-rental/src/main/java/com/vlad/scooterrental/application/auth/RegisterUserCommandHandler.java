package com.vlad.scooterrental.application.auth;

import com.vlad.scooterrental.application.audit.AuditAction;
import com.vlad.scooterrental.application.audit.AuditMessage;
import com.vlad.scooterrental.application.audit.AuditRecorder;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.UserFactory;
import com.vlad.scooterrental.domain.model.Role;
import com.vlad.scooterrental.domain.repository.PasswordHasher;
import com.vlad.scooterrental.domain.repository.UserRepository;
import java.time.Instant;
import java.util.UUID;

public class RegisterUserCommandHandler {
  private final UserFactory userFactory;
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final AuditRecorder auditRecorder;

  public RegisterUserCommandHandler(
      UserFactory userFactory,
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      AuditRecorder auditRecorder) {
    this.userFactory = userFactory;
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.auditRecorder = auditRecorder;
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
    auditRecorder.record(
        new AuditMessage(
            UUID.randomUUID(),
            AuditAction.USER_REGISTERED,
            savedUser.id().toString(),
            "User",
            savedUser.id().toString(),
            Instant.now(),
            "Registered user " + savedUser.email().value()));
    return savedUser.id();
  }
}
