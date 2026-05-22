package com.vlad.scooterrental.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.value.RentalPeriod;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class RentalPeriodTest {

  private final Clock clock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);

  @Test
  void shouldRejectPastStartTime() {
    assertThrows(
        ValidationException.class,
        () ->
            RentalPeriod.of(
                LocalDateTime.of(2026, 1, 1, 9, 0), LocalDateTime.of(2026, 1, 1, 11, 0), clock));
  }

  @Test
  void shouldRejectInvalidTimeRange() {
    assertThrows(
        ValidationException.class,
        () ->
            RentalPeriod.of(
                LocalDateTime.of(2026, 1, 1, 12, 0), LocalDateTime.of(2026, 1, 1, 11, 0), clock));
  }
}
