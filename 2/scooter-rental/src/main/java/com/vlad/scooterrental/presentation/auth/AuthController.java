package com.vlad.scooterrental.presentation.auth;

import com.vlad.scooterrental.application.auth.AuthResult;
import com.vlad.scooterrental.application.auth.LoginCommand;
import com.vlad.scooterrental.application.auth.LoginCommandHandler;
import com.vlad.scooterrental.application.auth.RegisterUserCommand;
import com.vlad.scooterrental.application.auth.RegisterUserCommandHandler;
import com.vlad.scooterrental.presentation.dto.LoginRequest;
import com.vlad.scooterrental.presentation.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final RegisterUserCommandHandler registerUserCommandHandler;
  private final LoginCommandHandler loginCommandHandler;

  public AuthController(
      RegisterUserCommandHandler registerUserCommandHandler,
      LoginCommandHandler loginCommandHandler) {
    this.registerUserCommandHandler = registerUserCommandHandler;
    this.loginCommandHandler = loginCommandHandler;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResult> register(@Valid @RequestBody RegisterRequest request) {
    AuthResult result =
        registerUserCommandHandler.handle(
            new RegisterUserCommand(request.fullName(), request.email(), request.password()));
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PostMapping("/login")
  public AuthResult login(@Valid @RequestBody LoginRequest request) {
    return loginCommandHandler.handle(new LoginCommand(request.email(), request.password()));
  }
}
