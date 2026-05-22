package com.vlad.scooterrental.core.application.scooter.command;

import com.vlad.scooterrental.core.api.event.ScooterCreatedEvent;
import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.factory.ScooterFactory;
import com.vlad.scooterrental.core.domain.model.ScooterStatus;
import com.vlad.scooterrental.core.domain.repository.ScooterRepository;
import com.vlad.scooterrental.shared.event.EventPublisher;
import java.time.Instant;
import java.util.UUID;

public class CreateScooterCommandHandler {
  private final ScooterFactory scooterFactory;
  private final ScooterRepository scooterRepository;
  private final EventPublisher eventPublisher;

  public CreateScooterCommandHandler(
      ScooterFactory scooterFactory,
      ScooterRepository scooterRepository,
      EventPublisher eventPublisher) {
    this.scooterFactory = scooterFactory;
    this.scooterRepository = scooterRepository;
    this.eventPublisher = eventPublisher;
  }

  public UUID handle(CreateScooterCommand command) {
    ScooterStatus status = parseStatus(command.status());
    var scooter =
        scooterFactory.create(
            command.code(),
            command.model(),
            status,
            command.batteryLevel(),
            command.pricePerMinute());
    var savedScooter = scooterRepository.save(scooter);
    eventPublisher.publish(
        new ScooterCreatedEvent(
            UUID.randomUUID(),
            savedScooter.id(),
            savedScooter.code(),
            savedScooter.model(),
            savedScooter.status().name(),
            "system",
            Instant.now()));
    return savedScooter.id();
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
