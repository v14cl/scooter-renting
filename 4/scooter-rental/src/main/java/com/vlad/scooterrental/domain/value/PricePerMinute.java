package com.vlad.scooterrental.domain.value;

import com.vlad.scooterrental.domain.exception.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record PricePerMinute(BigDecimal amount) {

  public PricePerMinute {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException("Price per minute must be greater than zero");
    }
    amount = amount.setScale(2, RoundingMode.HALF_UP);
  }

  public static PricePerMinute of(BigDecimal amount) {
    return new PricePerMinute(amount);
  }
}
