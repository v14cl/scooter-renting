package com.vlad.scooterrental.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.domain.value.PricePerMinute;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ScooterTest {

  @Test
  void shouldExposeRentalAvailabilityBehavior() {
    Scooter available = scooter(ScooterStatus.AVAILABLE);
    Scooter maintenance = scooter(ScooterStatus.MAINTENANCE);

    assertTrue(available.canBeRented());
    assertFalse(maintenance.canBeRented());
  }

  @Test
  void shouldRejectInvalidBatteryLevel() {
    assertThrows(
        ValidationException.class,
        () ->
            new Scooter(
                UUID.randomUUID(),
                "SCT-1",
                "Ninebot",
                ScooterStatus.AVAILABLE,
                101,
                PricePerMinute.of(new BigDecimal("2.50"))));
  }

  private Scooter scooter(ScooterStatus status) {
    return new Scooter(
        UUID.randomUUID(),
        "SCT-1",
        "Ninebot",
        status,
        80,
        PricePerMinute.of(new BigDecimal("2.50")));
  }
}
