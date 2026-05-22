package com.vlad.scooterrental.application.scooter.query;

import com.vlad.scooterrental.domain.exception.NotFoundException;
import java.util.UUID;

public class GetScooterByIdQueryHandler {
  private final ScooterReadRepository scooterReadRepository;

  public GetScooterByIdQueryHandler(ScooterReadRepository scooterReadRepository) {
    this.scooterReadRepository = scooterReadRepository;
  }

  public ScooterView handle(GetScooterByIdQuery query) {
    return scooterReadRepository
        .findViewById(query.scooterId())
        .orElseThrow(() -> new NotFoundException("Scooter not found"));
  }
}
