package com.vlad.scooterrental.domain.repository;

import com.vlad.scooterrental.domain.model.Rental;
import com.vlad.scooterrental.domain.value.RentalPeriod;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalRepository {

  Optional<Rental> findById(UUID id);

  List<Rental> findAll();

  List<Rental> findByRenterId(UUID renterId);

  boolean hasOverlap(UUID scooterId, RentalPeriod rentalPeriod, UUID ignoredRentalId);

  Rental save(Rental rental);

  void deleteById(UUID id);
}
