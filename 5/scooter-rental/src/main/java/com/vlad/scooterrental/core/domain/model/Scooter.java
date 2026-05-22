package com.vlad.scooterrental.core.domain.model;

import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.value.PricePerMinute;
import java.util.UUID;

public record Scooter(
    UUID id,
    String code,
    String model,
    ScooterStatus status,
    int batteryLevel,
    PricePerMinute pricePerMinute) {
  public Scooter {
    if (id == null) {
      throw new ValidationException("Scooter id must be provided");
    }
    if (code == null || code.isBlank()) {
      throw new ValidationException("Scooter code must not be blank");
    }
    if (model == null || model.isBlank()) {
      throw new ValidationException("Scooter model must not be blank");
    }
    if (status == null) {
      throw new ValidationException("Scooter status must be provided");
    }
    if (batteryLevel < 0 || batteryLevel > 100) {
      throw new ValidationException("Battery level must be between 0 and 100");
    }
    if (pricePerMinute == null) {
      throw new ValidationException("Price per minute must be provided");
    }
    code = code.trim().toUpperCase();
    model = model.trim();
  }

  public boolean canBeRented() {
    return status == ScooterStatus.AVAILABLE;
  }
}
