package com.vlad.scooterrental.application;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vlad.scooterrental.application.common.Actor;
import com.vlad.scooterrental.application.rental.command.UpdateRentalCommand;
import com.vlad.scooterrental.application.rental.command.UpdateRentalCommandHandler;
import com.vlad.scooterrental.domain.exception.AccessDeniedException;
import com.vlad.scooterrental.domain.factory.RentalFactory;
import com.vlad.scooterrental.domain.model.Rental;
import com.vlad.scooterrental.domain.model.Role;
import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.domain.repository.RentalRepository;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import com.vlad.scooterrental.domain.value.PricePerMinute;
import com.vlad.scooterrental.domain.value.RentalPeriod;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UpdateRentalCommandHandlerTest {

  @Test
  void shouldRejectUpdateForAnotherUsersRental() {
    Clock clock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);
    UUID ownerId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID scooterId = UUID.randomUUID();
    Rental rental =
        new Rental(
            UUID.randomUUID(),
            ownerId,
            scooterId,
            RentalPeriod.restore(
                LocalDateTime.of(2026, 1, 1, 11, 0), LocalDateTime.of(2026, 1, 1, 12, 0)));

    InMemoryRentalRepository rentalRepository = new InMemoryRentalRepository(rental);
    InMemoryScooterRepository scooterRepository =
        new InMemoryScooterRepository(
            new Scooter(
                scooterId,
                "SCT-77",
                "Ninebot",
                ScooterStatus.AVAILABLE,
                77,
                PricePerMinute.of(new BigDecimal("1.90"))));
    UpdateRentalCommandHandler handler =
        new UpdateRentalCommandHandler(
            new RentalFactory(clock, rentalRepository, scooterRepository),
            rentalRepository,
            scooterRepository);

    assertThrows(
        AccessDeniedException.class,
        () ->
            handler.handle(
                new UpdateRentalCommand(
                    new Actor(actorId, Role.CUSTOMER),
                    rental.id(),
                    scooterId,
                    LocalDateTime.of(2026, 1, 1, 13, 0),
                    LocalDateTime.of(2026, 1, 1, 14, 0))));
  }

  private record InMemoryRentalRepository(Rental rental) implements RentalRepository {
    @Override
    public Optional<Rental> findById(UUID id) {
      return Optional.of(rental);
    }

    @Override
    public List<Rental> findAll() {
      return List.of(rental);
    }

    @Override
    public List<Rental> findByRenterId(UUID renterId) {
      return List.of(rental);
    }

    @Override
    public boolean hasOverlap(UUID scooterId, RentalPeriod rentalPeriod, UUID ignoredRentalId) {
      return false;
    }

    @Override
    public Rental save(Rental rental) {
      return rental;
    }

    @Override
    public void deleteById(UUID id) {}
  }

  private record InMemoryScooterRepository(Scooter scooter) implements ScooterRepository {
    @Override
    public Optional<Scooter> findById(UUID id) {
      return Optional.of(scooter);
    }

    @Override
    public List<Scooter> findAll() {
      return List.of(scooter);
    }

    @Override
    public boolean existsByCode(String code) {
      return false;
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, UUID id) {
      return false;
    }

    @Override
    public Scooter save(Scooter scooter) {
      return scooter;
    }

    @Override
    public void deleteById(UUID id) {}
  }
}
