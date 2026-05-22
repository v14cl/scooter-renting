package com.vlad.scooterrental.core.domain.repository;

import com.vlad.scooterrental.core.domain.model.Scooter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScooterRepository {

  Optional<Scooter> findById(UUID id);

  List<Scooter> findAll();

  boolean existsByCode(String code);

  boolean existsByCodeAndIdNot(String code, UUID id);

  Scooter save(Scooter scooter);

  void deleteById(UUID id);
}
