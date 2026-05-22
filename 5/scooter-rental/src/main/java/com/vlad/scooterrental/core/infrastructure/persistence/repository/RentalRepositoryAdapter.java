package com.vlad.scooterrental.core.infrastructure.persistence.repository;

import com.vlad.scooterrental.core.domain.model.Rental;
import com.vlad.scooterrental.core.domain.repository.RentalRepository;
import com.vlad.scooterrental.core.domain.value.RentalPeriod;
import com.vlad.scooterrental.core.infrastructure.persistence.jpa.RentalJpaRepository;
import com.vlad.scooterrental.core.infrastructure.persistence.mapper.RentalEntityMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class RentalRepositoryAdapter implements RentalRepository {

  private final RentalJpaRepository rentalJpaRepository;
  private final RentalEntityMapper rentalEntityMapper;

  public RentalRepositoryAdapter(
      RentalJpaRepository rentalJpaRepository, RentalEntityMapper rentalEntityMapper) {
    this.rentalJpaRepository = rentalJpaRepository;
    this.rentalEntityMapper = rentalEntityMapper;
  }

  @Override
  public Optional<Rental> findById(UUID id) {
    return rentalJpaRepository.findById(id).map(rentalEntityMapper::toDomain);
  }

  @Override
  public List<Rental> findAll() {
    return rentalJpaRepository.findAllByOrderByStartTimeAsc().stream()
        .map(rentalEntityMapper::toDomain)
        .toList();
  }

  @Override
  public List<Rental> findByRenterId(UUID renterId) {
    return rentalJpaRepository.findAllByRenterIdOrderByStartTimeAsc(renterId).stream()
        .map(rentalEntityMapper::toDomain)
        .toList();
  }

  @Override
  public boolean hasOverlap(UUID scooterId, RentalPeriod rentalPeriod, UUID ignoredRentalId) {
    return rentalJpaRepository.hasOverlap(
        scooterId, rentalPeriod.startTime(), rentalPeriod.endTime(), ignoredRentalId);
  }

  @Override
  public Rental save(Rental rental) {
    return rentalEntityMapper.toDomain(
        rentalJpaRepository.save(rentalEntityMapper.toEntity(rental)));
  }

  @Override
  public void deleteById(UUID id) {
    rentalJpaRepository.deleteById(id);
  }
}
