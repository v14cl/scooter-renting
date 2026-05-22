package com.vlad.scooterrental.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.value.Email;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void shouldNormalizeValidEmail() {
    Email email = Email.of("  USER@Example.com ");

    assertEquals("user@example.com", email.value());
  }

  @Test
  void shouldRejectInvalidEmail() {
    assertThrows(ValidationException.class, () -> Email.of("wrong-email"));
  }
}
