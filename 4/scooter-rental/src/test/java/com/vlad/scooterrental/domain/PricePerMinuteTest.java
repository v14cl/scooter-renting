package com.vlad.scooterrental.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.value.PricePerMinute;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PricePerMinuteTest {

  @Test
  void shouldNormalizeScale() {
    PricePerMinute price = PricePerMinute.of(new BigDecimal("2.505"));

    assertEquals(new BigDecimal("2.51"), price.amount());
  }

  @Test
  void shouldRejectZeroOrNegativeAmount() {
    assertThrows(ValidationException.class, () -> PricePerMinute.of(BigDecimal.ZERO));
    assertThrows(ValidationException.class, () -> PricePerMinute.of(new BigDecimal("-1.00")));
  }
}
