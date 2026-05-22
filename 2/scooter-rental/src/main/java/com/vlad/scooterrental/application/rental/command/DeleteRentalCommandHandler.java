package com.vlad.scooterrental.application.rental.command;

import com.vlad.scooterrental.domain.exception.AccessDeniedException;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.repository.RentalRepository;

public class DeleteRentalCommandHandler {
  private final RentalRepository rentalRepository;

  public DeleteRentalCommandHandler(RentalRepository rentalRepository) {
    this.rentalRepository = rentalRepository;
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
  }
}
