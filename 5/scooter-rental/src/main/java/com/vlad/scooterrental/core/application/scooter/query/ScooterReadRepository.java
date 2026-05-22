package com.vlad.scooterrental.core.application.scooter.query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScooterReadRepository {
  List<ScooterView> findAllViews(String status);

  Optional<ScooterView> findViewById(UUID scooterId);
}
