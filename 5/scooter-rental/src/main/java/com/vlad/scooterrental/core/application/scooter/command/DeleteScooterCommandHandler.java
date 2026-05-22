package com.vlad.scooterrental.core.application.scooter.command;

import com.vlad.scooterrental.core.api.event.ScooterDeletedEvent;
import com.vlad.scooterrental.core.domain.exception.NotFoundException;
import com.vlad.scooterrental.core.domain.repository.ScooterRepository;
import com.vlad.scooterrental.shared.event.EventPublisher;
import java.time.Instant;
import java.util.UUID;

public class DeleteScooterCommandHandler {
  private final ScooterRepository scooterRepository;
  private final EventPublisher eventPublisher;

  public DeleteScooterCommandHandler(
      ScooterRepository scooterRepository, EventPublisher eventPublisher) {
    this.scooterRepository = scooterRepository;
    this.eventPublisher = eventPublisher;
  }

  public void handle(DeleteScooterCommand command) {
    var scooter =
        scooterRepository
        .findById(command.scooterId())
        .orElseThrow(() -> new NotFoundException("Scooter not found"));
    scooterRepository.deleteById(command.scooterId());
    eventPublisher.publish(
        new ScooterDeletedEvent(
            UUID.randomUUID(),
            scooter.id(),
            scooter.code(),
            "system",
            Instant.now()));
  }
}
