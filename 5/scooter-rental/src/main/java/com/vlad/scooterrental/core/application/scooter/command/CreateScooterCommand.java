package com.vlad.scooterrental.core.application.scooter.command;

import java.math.BigDecimal;

public record CreateScooterCommand(
    String code, String model, String status, int batteryLevel, BigDecimal pricePerMinute) {}
