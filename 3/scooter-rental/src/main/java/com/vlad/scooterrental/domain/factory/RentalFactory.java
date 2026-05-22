package com.vlad.scooterrental.domain.factory;

import com.vlad.scooterrental.domain.exception.ConflictException;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.model.Rental;
import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.domain.repository.RentalRepository;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import com.vlad.scooterrental.domain.value.RentalPeriod;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class RentalFactory {

    private final Clock clock;

    public RentalFactory(Clock clock) {
        this.clock = clock;
    }

    public Rental create(
            UUID renterId,
            UUID scooterId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            RentalRepository rentalRepository,
            ScooterRepository scooterRepository
    ) {
        validateIds(renterId, scooterId);

        RentalPeriod rentalPeriod = RentalPeriod.of(startTime, endTime, clock);
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new NotFoundException("Scooter not found"));
        validateScooterAvailability(scooter);
        validateOverlap(scooterId, rentalPeriod, null, rentalRepository);

        return new Rental(UUID.randomUUID(), renterId, scooterId, rentalPeriod);
    }

    public Rental update(
            UUID rentalId,
            UUID renterId,
            UUID scooterId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            RentalRepository rentalRepository,
            ScooterRepository scooterRepository
    ) {
        validateIds(renterId, scooterId);

        RentalPeriod rentalPeriod = RentalPeriod.of(startTime, endTime, clock);
        Scooter scooter = scooterRepository.findById(scooterId)
                .orElseThrow(() -> new NotFoundException("Scooter not found"));
        validateScooterAvailability(scooter);
        validateOverlap(scooterId, rentalPeriod, rentalId, rentalRepository);

        return new Rental(rentalId, renterId, scooterId, rentalPeriod);
    }

    private void validateIds(UUID renterId, UUID scooterId) {
        if (renterId == null) {
            throw new ValidationException("Renter id must be provided");
        }
        if (scooterId == null) {
            throw new ValidationException("Scooter id must be provided");
        }
    }

    private void validateScooterAvailability(Scooter scooter) {
        if (scooter.status() != ScooterStatus.AVAILABLE) {
            throw new ConflictException("Scooter is not available for booking");
        }
    }

    private void validateOverlap(
            UUID scooterId,
            RentalPeriod rentalPeriod,
            UUID ignoredRentalId,
            RentalRepository rentalRepository
    ) {
        if (rentalRepository.hasOverlap(scooterId, rentalPeriod, ignoredRentalId)) {
            throw new ConflictException("Rental time slot overlaps with an existing booking");
        }
    }
}
