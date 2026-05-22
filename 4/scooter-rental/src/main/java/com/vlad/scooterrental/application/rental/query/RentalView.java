package com.vlad.scooterrental.application.rental.query;

import com.vlad.scooterrental.domain.model.Rental;
import java.time.LocalDateTime;
import java.util.UUID;

public record RentalView(
    UUID id, UUID renterId, UUID scooterId, LocalDateTime startTime, LocalDateTime endTime) {
  public static RentalView from(Rental rental) {
    return new RentalView(
        rental.id(),
        rental.renterId(),
        rental.scooterId(),
        rental.period().startTime(),
        rental.period().endTime());
  }
}
