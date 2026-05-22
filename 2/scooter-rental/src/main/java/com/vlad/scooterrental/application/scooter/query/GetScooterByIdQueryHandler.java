package com.vlad.scooterrental.application.scooter.query;

import com.vlad.scooterrental.domain.exception.NotFoundException;
import java.util.UUID;

public class GetScooterByIdQueryHandler {
  private final ScooterReadRepository scooterReadRepository;

  public GetScooterByIdQueryHandler(ScooterReadRepository scooterReadRepository) {
    this.scooterReadRepository = scooterReadRepository;
  }

  public ScooterView handle(UUID scooterId) {
    return scooterReadRepository
        .findViewById(scooterId)
        .orElseThrow(() -> new NotFoundException("Scooter not found"));
  }
}
