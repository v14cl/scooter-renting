package com.vlad.scooterrental.domain.value;

import com.vlad.scooterrental.domain.exception.ValidationException;
import java.time.Clock;
import java.time.LocalDateTime;

public record RentalPeriod(LocalDateTime startTime, LocalDateTime endTime) {

  public RentalPeriod {
    if (startTime == null || endTime == null) {
      throw new ValidationException("Rental time range must be provided");
    }
    if (!startTime.isBefore(endTime)) {
      throw new ValidationException("Rental start time must be before end time");
    }
  }

  public static RentalPeriod of(LocalDateTime startTime, LocalDateTime endTime, Clock clock) {
    if (startTime.isBefore(LocalDateTime.now(clock))) {
      throw new ValidationException("Rental start time cannot be in the past");
    }
    return new RentalPeriod(startTime, endTime);
  }

  public static RentalPeriod restore(LocalDateTime startTime, LocalDateTime endTime) {
    return new RentalPeriod(startTime, endTime);
  }
}
