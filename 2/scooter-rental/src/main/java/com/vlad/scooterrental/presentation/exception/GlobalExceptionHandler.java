package com.vlad.scooterrental.presentation.exception;

import com.vlad.scooterrental.domain.exception.AuthenticationException;
import com.vlad.scooterrental.domain.exception.ConflictException;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.presentation.dto.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiError> handleValidation(ValidationException exception) {
    return build(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception) {
    String message =
        exception.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .orElse("Request validation failed");
    return build(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(
      ConstraintViolationException exception) {
    return build(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException exception) {
    return build(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiError> handleConflict(ConflictException exception) {
    return build(HttpStatus.CONFLICT, exception.getMessage());
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleAuthentication(AuthenticationException exception) {
    return build(HttpStatus.UNAUTHORIZED, exception.getMessage());
  }

  @ExceptionHandler({
    com.vlad.scooterrental.domain.exception.AccessDeniedException.class,
    AccessDeniedException.class
  })
  public ResponseEntity<ApiError> handleAccessDenied(RuntimeException exception) {
    return build(HttpStatus.FORBIDDEN, exception.getMessage());
  }

  private ResponseEntity<ApiError> build(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new ApiError(status.value(), message));
  }
}
