package com.vlad.scooterrental.core.application.rental.command;

import com.vlad.scooterrental.shared.event.EventPublisher;
import com.vlad.scooterrental.core.api.event.RentalUpdatedEvent;
import com.vlad.scooterrental.core.domain.exception.AccessDeniedException;
import com.vlad.scooterrental.core.domain.exception.NotFoundException;
import com.vlad.scooterrental.core.domain.factory.RentalFactory;
import com.vlad.scooterrental.core.domain.repository.RentalRepository;
import java.time.Instant;
import java.util.UUID;

public class UpdateRentalCommandHandler {
  private final RentalFactory rentalFactory;
  private final RentalRepository rentalRepository;
  private final EventPublisher eventPublisher;

  public UpdateRentalCommandHandler(
      RentalFactory rentalFactory, RentalRepository rentalRepository, EventPublisher eventPublisher) {
    this.rentalFactory = rentalFactory;
    this.rentalRepository = rentalRepository;
    this.eventPublisher = eventPublisher;
  }

  public void handle(UpdateRentalCommand command) {
    var existingRental =
        rentalRepository
            .findById(command.rentalId())
            .orElseThrow(() -> new NotFoundException("Rental not found"));
    if (!command.actor().isAdmin() && !existingRental.belongsTo(command.actor().userId())) {
      throw new AccessDeniedException("You cannot update this rental");
    }
    var updatedRental =
        rentalFactory.update(
            existingRental.id(),
            existingRental.renterId(),
            command.scooterId(),
            command.startTime(),
            command.endTime());
    rentalRepository.save(updatedRental);
    eventPublisher.publish(
        new RentalUpdatedEvent(
            UUID.randomUUID(),
            updatedRental.id(),
            updatedRental.scooterId(),
            updatedRental.renterId(),
            command.actor().userId(),
            updatedRental.period().startTime(),
            updatedRental.period().endTime(),
            Instant.now()));
  }
}
