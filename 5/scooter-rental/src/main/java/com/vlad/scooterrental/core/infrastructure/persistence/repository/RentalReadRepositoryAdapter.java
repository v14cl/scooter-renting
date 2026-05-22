package com.vlad.scooterrental.core.infrastructure.persistence.repository;

import com.vlad.scooterrental.core.application.common.Actor;
import com.vlad.scooterrental.core.application.rental.query.RentalReadRepository;
import com.vlad.scooterrental.core.application.rental.query.RentalView;
import com.vlad.scooterrental.core.infrastructure.persistence.jpa.RentalJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class RentalReadRepositoryAdapter implements RentalReadRepository {

  private final RentalJpaRepository rentalJpaRepository;

  public RentalReadRepositoryAdapter(RentalJpaRepository rentalJpaRepository) {
    this.rentalJpaRepository = rentalJpaRepository;
  }

  @Override
  public List<RentalView> findAllVisibleFor(Actor actor) {
    var rentals =
        actor.isAdmin()
            ? rentalJpaRepository.findAllByOrderByStartTimeAsc()
            : rentalJpaRepository.findAllByRenterIdOrderByStartTimeAsc(actor.userId());

    return rentals.stream()
        .map(
            entity ->
                new RentalView(
                    entity.getId(),
                    entity.getRenterId(),
                    entity.getScooterId(),
                    entity.getStartTime(),
                    entity.getEndTime()))
        .toList();
  }

  @Override
  public Optional<RentalView> findVisibleById(UUID rentalId, Actor actor) {
    return rentalJpaRepository
        .findById(rentalId)
        .filter(entity -> actor.isAdmin() || entity.getRenterId().equals(actor.userId()))
        .map(
            entity ->
                new RentalView(
                    entity.getId(),
                    entity.getRenterId(),
                    entity.getScooterId(),
                    entity.getStartTime(),
                    entity.getEndTime()));
  }
}
