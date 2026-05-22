package com.vlad.scooterrental.application.scooter.command;

import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.model.ScooterStatus;

import java.math.BigDecimal;

public record CreateScooterCommand(
        String code,
        String model,
        ScooterStatus status,
        int batteryLevel,
        BigDecimal pricePerMinute
) {
    public static CreateScooterCommand of(
            String code,
            String model,
            String statusName,
            int batteryLevel,
            BigDecimal pricePerMinute
    ) {
        if (statusName == null || statusName.isBlank()) {
            throw new ValidationException("Scooter status must be provided");
        }
        try {
            return new CreateScooterCommand(
                    code,
                    model,
                    ScooterStatus.valueOf(statusName.trim().toUpperCase()),
                    batteryLevel,
                    pricePerMinute
            );
        } catch (IllegalArgumentException exception) {
            throw new ValidationException("Unsupported scooter status: " + statusName);
        }
    }
}
