package com.vlad.scooterrental.core.application.scooter.command;

import com.vlad.scooterrental.core.api.event.ScooterUpdatedEvent;
import com.vlad.scooterrental.core.domain.exception.NotFoundException;
import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.factory.ScooterFactory;
import com.vlad.scooterrental.core.domain.model.ScooterStatus;
import com.vlad.scooterrental.core.domain.repository.ScooterRepository;
import com.vlad.scooterrental.shared.event.EventPublisher;
import java.time.Instant;
import java.util.UUID;

public class UpdateScooterCommandHandler {
  private final ScooterFactory scooterFactory;
  private final ScooterRepository scooterRepository;
  private final EventPublisher eventPublisher;

  public UpdateScooterCommandHandler(
      ScooterFactory scooterFactory,
      ScooterRepository scooterRepository,
      EventPublisher eventPublisher) {
    this.scooterFactory = scooterFactory;
    this.scooterRepository = scooterRepository;
    this.eventPublisher = eventPublisher;
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
    eventPublisher.publish(
        new ScooterUpdatedEvent(
            UUID.randomUUID(),
            scooter.id(),
            scooter.code(),
            scooter.model(),
            scooter.status().name(),
            "system",
            Instant.now()));
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
