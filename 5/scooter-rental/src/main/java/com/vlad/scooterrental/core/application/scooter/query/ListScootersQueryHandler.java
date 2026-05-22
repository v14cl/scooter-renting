package com.vlad.scooterrental.core.application.scooter.query;

import java.util.List;

public class ListScootersQueryHandler {
  private final ScooterReadRepository scooterReadRepository;

  public ListScootersQueryHandler(ScooterReadRepository scooterReadRepository) {
    this.scooterReadRepository = scooterReadRepository;
  }

  public List<ScooterView> handle(ListScootersQuery query) {
    return scooterReadRepository.findAllViews(query.status());
  }
}
