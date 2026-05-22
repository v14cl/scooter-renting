package com.vlad.scooterrental.application.scooter.command;

import com.vlad.scooterrental.application.scooter.query.ScooterView;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.factory.ScooterFactory;
import com.vlad.scooterrental.domain.repository.ScooterRepository;

public class UpdateScooterCommandHandler {
  private final ScooterFactory scooterFactory;
  private final ScooterRepository scooterRepository;

  public UpdateScooterCommandHandler(
      ScooterFactory scooterFactory, ScooterRepository scooterRepository) {
    this.scooterFactory = scooterFactory;
    this.scooterRepository = scooterRepository;
  }

  public ScooterView handle(UpdateScooterCommand command) {
    scooterRepository
        .findById(command.scooterId())
        .orElseThrow(() -> new NotFoundException("Scooter not found"));
    var scooter =
        scooterFactory.update(
            command.scooterId(),
            command.code(),
            command.model(),
            command.status(),
            command.batteryLevel(),
            command.pricePerMinute());
    return ScooterView.from(scooterRepository.save(scooter));
  }
}
