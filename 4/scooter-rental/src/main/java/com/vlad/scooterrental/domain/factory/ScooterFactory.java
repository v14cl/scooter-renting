package com.vlad.scooterrental.domain.factory;

import com.vlad.scooterrental.domain.exception.ConflictException;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import com.vlad.scooterrental.domain.value.PricePerMinute;
import java.math.BigDecimal;
import java.util.UUID;

public class ScooterFactory {

  private final ScooterRepository scooterRepository;

  public ScooterFactory(ScooterRepository scooterRepository) {
    this.scooterRepository = scooterRepository;
  }

  public Scooter create(
      String code,
      String model,
      ScooterStatus status,
      int batteryLevel,
      BigDecimal pricePerMinute) {
    String normalizedCode = validateCode(code);
    validateCommonFields(model, status, batteryLevel, pricePerMinute);

    if (scooterRepository.existsByCode(normalizedCode)) {
      throw new ConflictException("Scooter code already exists");
    }

    return new Scooter(
        UUID.randomUUID(),
        normalizedCode,
        model.trim(),
        status,
        batteryLevel,
        PricePerMinute.of(pricePerMinute));
  }

  public Scooter update(
      UUID scooterId,
      String code,
      String model,
      ScooterStatus status,
      int batteryLevel,
      BigDecimal pricePerMinute) {
    String normalizedCode = validateCode(code);
    validateCommonFields(model, status, batteryLevel, pricePerMinute);

    if (scooterRepository.existsByCodeAndIdNot(normalizedCode, scooterId)) {
      throw new ConflictException("Scooter code already exists");
    }

    return new Scooter(
        scooterId,
        normalizedCode,
        model.trim(),
        status,
        batteryLevel,
        PricePerMinute.of(pricePerMinute));
  }

  private void validateCommonFields(
      String model, ScooterStatus status, int batteryLevel, BigDecimal pricePerMinute) {
    if (model == null || model.isBlank()) {
      throw new ValidationException("Scooter model must not be blank");
    }
    if (status == null) {
      throw new ValidationException("Scooter status must be provided");
    }
    if (batteryLevel < 0 || batteryLevel > 100) {
      throw new ValidationException("Battery level must be between 0 and 100");
    }
    if (pricePerMinute == null || pricePerMinute.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException("Price per minute must be greater than zero");
    }
  }

  private String validateCode(String code) {
    if (code == null || code.isBlank()) {
      throw new ValidationException("Scooter code must not be blank");
    }
    return code.trim().toUpperCase();
  }
}
