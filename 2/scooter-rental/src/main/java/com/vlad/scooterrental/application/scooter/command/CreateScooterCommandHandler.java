package com.vlad.scooterrental.application.scooter.command;

import com.vlad.scooterrental.application.scooter.query.ScooterView;
import com.vlad.scooterrental.domain.factory.ScooterFactory;
import com.vlad.scooterrental.domain.repository.ScooterRepository;

public class CreateScooterCommandHandler {
  private final ScooterFactory scooterFactory;
  private final ScooterRepository scooterRepository;

  public CreateScooterCommandHandler(
      ScooterFactory scooterFactory, ScooterRepository scooterRepository) {
    this.scooterFactory = scooterFactory;
    this.scooterRepository = scooterRepository;
  }

  public ScooterView handle(CreateScooterCommand command) {
    var scooter =
        scooterFactory.create(
            command.code(),
            command.model(),
            command.status(),
            command.batteryLevel(),
            command.pricePerMinute());
    return ScooterView.from(scooterRepository.save(scooter));
  }
}
