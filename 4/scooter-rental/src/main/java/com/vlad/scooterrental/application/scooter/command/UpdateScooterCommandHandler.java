package com.vlad.scooterrental.application.scooter.command;

import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.ScooterFactory;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.domain.repository.ScooterRepository;

public class UpdateScooterCommandHandler {
  private final ScooterFactory scooterFactory;
  private final ScooterRepository scooterRepository;

  public UpdateScooterCommandHandler(
      ScooterFactory scooterFactory, ScooterRepository scooterRepository) {
    this.scooterFactory = scooterFactory;
    this.scooterRepository = scooterRepository;
  }

  public void handle(UpdateScooterCommand command) {
    scooterRepository
        .findById(command.scooterId())
        .orElseThrow(() -> new NotFoundException("Scooter not found"));
    var scooter =
        scooterFactory.update(
            command.scooterId(),
            command.code(),
            command.model(),
            parseStatus(command.status()),
            command.batteryLevel(),
            command.pricePerMinute());
    scooterRepository.save(scooter);
  }

  private ScooterStatus parseStatus(String statusName) {
    if (statusName == null || statusName.isBlank()) {
      throw new ValidationException("Scooter status must be provided");
    }
    try {
      return ScooterStatus.valueOf(statusName.trim().toUpperCase());
    } catch (IllegalArgumentException exception) {
      throw new ValidationException("Unsupported scooter status: " + statusName);
    }
  }
}
