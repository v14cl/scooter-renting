package com.vlad.scooterrental.core.domain.value;

import com.vlad.scooterrental.core.domain.exception.ValidationException;
import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  public Email {
    if (value == null || value.isBlank()) {
      throw new ValidationException("Email must not be blank");
    }

    String normalized = value.trim().toLowerCase(Locale.ROOT);
    if (!EMAIL_PATTERN.matcher(normalized).matches()) {
      throw new ValidationException("Email must be valid");
    }

    value = normalized;
  }

  public static Email of(String rawValue) {
    return new Email(rawValue);
  }

  @Override
  public String toString() {
    return value;
  }
}
