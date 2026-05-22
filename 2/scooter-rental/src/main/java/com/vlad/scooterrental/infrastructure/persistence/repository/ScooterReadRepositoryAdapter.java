package com.vlad.scooterrental.infrastructure.persistence.repository;

import com.vlad.scooterrental.application.scooter.query.ScooterReadRepository;
import com.vlad.scooterrental.application.scooter.query.ScooterView;
import com.vlad.scooterrental.infrastructure.persistence.jpa.ScooterJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ScooterReadRepositoryAdapter implements ScooterReadRepository {

  private final ScooterJpaRepository scooterJpaRepository;

  public ScooterReadRepositoryAdapter(ScooterJpaRepository scooterJpaRepository) {
    this.scooterJpaRepository = scooterJpaRepository;
  }

  @Override
  public List<ScooterView> findAllViews() {
    return scooterJpaRepository.findAll(Sort.by("code").ascending()).stream()
        .map(
            entity ->
                new ScooterView(
                    entity.getId(),
                    entity.getCode(),
                    entity.getModel(),
                    entity.getStatus(),
                    entity.getBatteryLevel(),
                    entity.getPricePerMinute()))
        .toList();
  }

  @Override
  public Optional<ScooterView> findViewById(UUID scooterId) {
    return scooterJpaRepository
        .findById(scooterId)
        .map(
            entity ->
                new ScooterView(
                    entity.getId(),
                    entity.getCode(),
                    entity.getModel(),
                    entity.getStatus(),
                    entity.getBatteryLevel(),
                    entity.getPricePerMinute()));
  }
}
