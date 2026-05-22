package com.vlad.scooterrental.core.application.rental.command;

import com.vlad.scooterrental.shared.event.EventPublisher;
import com.vlad.scooterrental.core.api.event.RentalDeletedEvent;
import com.vlad.scooterrental.core.domain.exception.AccessDeniedException;
import com.vlad.scooterrental.core.domain.exception.NotFoundException;
import com.vlad.scooterrental.core.domain.repository.RentalRepository;
import java.time.Instant;
import java.util.UUID;

public class DeleteRentalCommandHandler {
  private final RentalRepository rentalRepository;
  private final EventPublisher eventPublisher;

  public DeleteRentalCommandHandler(RentalRepository rentalRepository, EventPublisher eventPublisher) {
    this.rentalRepository = rentalRepository;
    this.eventPublisher = eventPublisher;
  }

  public void handle(DeleteRentalCommand command) {
    var rental =
        rentalRepository
            .findById(command.rentalId())
            .orElseThrow(() -> new NotFoundException("Rental not found"));
    if (!command.actor().isAdmin() && !rental.belongsTo(command.actor().userId())) {
      throw new AccessDeniedException("You cannot delete this rental");
    }
    rentalRepository.deleteById(command.rentalId());
    eventPublisher.publish(
        new RentalDeletedEvent(
            UUID.randomUUID(),
            rental.id(),
            rental.scooterId(),
            rental.renterId(),
            command.actor().userId(),
            rental.period().startTime(),
            rental.period().endTime(),
            Instant.now()));
  }
}
