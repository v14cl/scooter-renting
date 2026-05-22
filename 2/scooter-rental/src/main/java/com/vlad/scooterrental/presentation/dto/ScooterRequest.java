package com.vlad.scooterrental.presentation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ScooterRequest(
    @NotBlank String code,
    @NotBlank String model,
    @NotBlank String status,
    @Min(0) @Max(100) int batteryLevel,
    @NotNull @DecimalMin("0.01") BigDecimal pricePerMinute) {}
