package com.vlad.scooterrental.application.scooter.command;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateScooterCommand(
    UUID scooterId,
    String code,
    String model,
    String status,
    int batteryLevel,
    BigDecimal pricePerMinute) {}
