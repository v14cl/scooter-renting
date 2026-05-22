package com.vlad.scooterrental.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Scooter(
        UUID id,
        String code,
        String model,
        ScooterStatus status,
        int batteryLevel,
        BigDecimal pricePerMinute
) {
}
