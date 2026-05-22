package com.vlad.scooterrental.application.auth;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.UserFactory;
import com.vlad.scooterrental.domain.model.Role;
import com.vlad.scooterrental.domain.model.User;
import com.vlad.scooterrental.domain.repository.PasswordHasher;
import com.vlad.scooterrental.domain.repository.TokenProvider;
import com.vlad.scooterrental.domain.repository.UserRepository;

public class RegisterUserCommandHandler {
  private final UserFactory userFactory;
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final TokenProvider tokenProvider;

  public RegisterUserCommandHandler(
      UserFactory userFactory,
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      TokenProvider tokenProvider) {
    this.userFactory = userFactory;
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.tokenProvider = tokenProvider;
  }

  public AuthResult handle(RegisterUserCommand command) {
    if (command.password() == null || command.password().isBlank()) {
      throw new ValidationException("Password must not be blank");
    }
    User user =
        userFactory.create(
            command.fullName(),
            command.email(),
            passwordHasher.hash(command.password()),
            Role.CUSTOMER);
    User savedUser = userRepository.save(user);
    return new AuthResult(tokenProvider.issueToken(savedUser), UserView.from(savedUser));
  }
}
