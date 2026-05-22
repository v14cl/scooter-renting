package com.vlad.scooterrental.domain;

import com.vlad.scooterrental.domain.exception.ConflictException;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.factory.RentalFactory;
import com.vlad.scooterrental.domain.model.Rental;
import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.domain.repository.RentalRepository;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import com.vlad.scooterrental.domain.value.RentalPeriod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RentalFactoryTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);
    private final RentalFactory rentalFactory = new RentalFactory(clock);

    @Test
    void shouldRejectOverlappingRental() {
        UUID scooterId = UUID.randomUUID();
        UUID renterId = UUID.randomUUID();
        Scooter scooter = new Scooter(
                scooterId,
                "SCT-1",
                "Xiaomi",
                ScooterStatus.AVAILABLE,
                80,
                new BigDecimal("2.50")
        );

        assertThrows(ConflictException.class, () -> rentalFactory.create(
                renterId,
                scooterId,
                LocalDateTime.of(2026, 1, 1, 11, 0),
                LocalDateTime.of(2026, 1, 1, 12, 0),
                new FakeRentalRepository(true),
                new FakeScooterRepository(scooter)
        ));
    }

    @Test
    void shouldRejectUnknownScooter() {
        assertThrows(NotFoundException.class, () -> rentalFactory.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 1, 1, 11, 0),
                LocalDateTime.of(2026, 1, 1, 12, 0),
                new FakeRentalRepository(false),
                new FakeScooterRepository(null)
        ));
    }

    private record FakeRentalRepository(boolean overlap) implements RentalRepository {
        @Override
        public Optional<Rental> findById(UUID id) {
            return Optional.empty();
        }

        @Override
        public List<Rental> findAll() {
            return List.of();
        }

        @Override
        public List<Rental> findByRenterId(UUID renterId) {
            return List.of();
        }

        @Override
        public boolean hasOverlap(UUID scooterId, RentalPeriod rentalPeriod, UUID ignoredRentalId) {
            return overlap;
        }

        @Override
        public Rental save(Rental rental) {
            return rental;
        }

        @Override
        public void deleteById(UUID id) {
        }
    }

    private record FakeScooterRepository(Scooter scooter) implements ScooterRepository {
        @Override
        public Optional<Scooter> findById(UUID id) {
            return Optional.ofNullable(scooter);
        }

        @Override
        public List<Scooter> findAll() {
            return List.of();
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
        public void deleteById(UUID id) {
        }
    }
}
