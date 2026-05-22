package com.vlad.scooterrental.domain.model;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.value.RentalPeriod;
import java.util.UUID;

public record Rental(UUID id, UUID renterId, UUID scooterId, RentalPeriod period) {
  public Rental {
    if (id == null) {
      throw new ValidationException("Rental id must be provided");
    }
    if (renterId == null) {
      throw new ValidationException("Renter id must be provided");
    }
    if (scooterId == null) {
      throw new ValidationException("Scooter id must be provided");
    }
    if (period == null) {
      throw new ValidationException("Rental period must be provided");
    }
  }

  public boolean belongsTo(UUID userId) {
    return renterId.equals(userId);
  }
}
