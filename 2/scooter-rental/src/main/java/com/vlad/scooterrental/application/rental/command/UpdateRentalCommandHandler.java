package com.vlad.scooterrental.application.rental.command;

import com.vlad.scooterrental.application.rental.query.RentalView;
import com.vlad.scooterrental.domain.exception.AccessDeniedException;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.factory.RentalFactory;
import com.vlad.scooterrental.domain.repository.RentalRepository;
import com.vlad.scooterrental.domain.repository.ScooterRepository;

public class UpdateRentalCommandHandler {
  private final RentalFactory rentalFactory;
  private final RentalRepository rentalRepository;
  private final ScooterRepository scooterRepository;

  public UpdateRentalCommandHandler(
      RentalFactory rentalFactory,
      RentalRepository rentalRepository,
      ScooterRepository scooterRepository) {
    this.rentalFactory = rentalFactory;
    this.rentalRepository = rentalRepository;
    this.scooterRepository = scooterRepository;
  }

  public RentalView handle(UpdateRentalCommand command) {
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
    return RentalView.from(rentalRepository.save(updatedRental));
  }
}
