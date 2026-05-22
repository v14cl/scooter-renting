package com.vlad.scooterrental.application.scooter.command;

import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.repository.ScooterRepository;

public class DeleteScooterCommandHandler {
  private final ScooterRepository scooterRepository;

  public DeleteScooterCommandHandler(ScooterRepository scooterRepository) {
    this.scooterRepository = scooterRepository;
  }

  public void handle(DeleteScooterCommand command) {
    scooterRepository
        .findById(command.scooterId())
        .orElseThrow(() -> new NotFoundException("Scooter not found"));
    scooterRepository.deleteById(command.scooterId());
  }
}
