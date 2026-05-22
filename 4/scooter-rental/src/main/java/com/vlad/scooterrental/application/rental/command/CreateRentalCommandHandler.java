package com.vlad.scooterrental.application.rental.command;

import com.vlad.scooterrental.application.event.EventPublisher;
import com.vlad.scooterrental.application.event.events.RentalCreatedEvent;
import com.vlad.scooterrental.domain.factory.RentalFactory;
import com.vlad.scooterrental.domain.repository.RentalRepository;
import java.time.Instant;
import java.util.UUID;

public class CreateRentalCommandHandler {
  private final RentalFactory rentalFactory;
  private final RentalRepository rentalRepository;
  private final EventPublisher eventPublisher;

  public CreateRentalCommandHandler(
      RentalFactory rentalFactory, RentalRepository rentalRepository, EventPublisher eventPublisher) {
    this.rentalFactory = rentalFactory;
    this.rentalRepository = rentalRepository;
    this.eventPublisher = eventPublisher;
  }

  public UUID handle(CreateRentalCommand command) {
    var rental =
        rentalFactory.create(
            command.actor().userId(), command.scooterId(), command.startTime(), command.endTime());
    var savedRental = rentalRepository.save(rental);
    eventPublisher.publish(
        new RentalCreatedEvent(
            UUID.randomUUID(),
            savedRental.id(),
            savedRental.scooterId(),
            savedRental.renterId(),
            command.actor().userId(),
            savedRental.period().startTime(),
            savedRental.period().endTime(),
            Instant.now()));
    return savedRental.id();
  }
}
