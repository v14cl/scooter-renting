package com.vlad.scooterrental.application.scooter.query;

import com.vlad.scooterrental.domain.model.Scooter;

import java.math.BigDecimal;
import java.util.UUID;

public record ScooterView(
        UUID id,
        String code,
        String model,
        String status,
        int batteryLevel,
        BigDecimal pricePerMinute
) {

    public static ScooterView from(Scooter scooter) {
        return new ScooterView(
                scooter.id(),
                scooter.code(),
                scooter.model(),
                scooter.status().name(),
                scooter.batteryLevel(),
                scooter.pricePerMinute()
        );
    }
}
