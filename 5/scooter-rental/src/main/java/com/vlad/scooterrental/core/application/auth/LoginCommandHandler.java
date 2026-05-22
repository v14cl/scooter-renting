package com.vlad.scooterrental.core.application.auth;

import com.vlad.scooterrental.core.application.user.query.UserView;
import com.vlad.scooterrental.core.domain.exception.AuthenticationException;
import com.vlad.scooterrental.core.domain.repository.PasswordHasher;
import com.vlad.scooterrental.core.domain.repository.TokenProvider;
import com.vlad.scooterrental.core.domain.repository.UserRepository;
import com.vlad.scooterrental.core.domain.value.Email;

public class LoginCommandHandler {
  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final TokenProvider tokenProvider;

  public LoginCommandHandler(
      UserRepository userRepository, PasswordHasher passwordHasher, TokenProvider tokenProvider) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.tokenProvider = tokenProvider;
  }

  public AuthResult handle(LoginCommand command) {
    var user =
        userRepository
            .findByEmail(Email.of(command.email()))
            .orElseThrow(() -> new AuthenticationException("Invalid email or password"));
    if (!passwordHasher.matches(command.password(), user.passwordHash())) {
      throw new AuthenticationException("Invalid email or password");
    }
    return new AuthResult(tokenProvider.issueToken(user), UserView.from(user));
  }
}
