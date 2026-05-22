package com.vlad.scooterrental.core.application.scooter.query;

import com.vlad.scooterrental.core.domain.exception.NotFoundException;
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
