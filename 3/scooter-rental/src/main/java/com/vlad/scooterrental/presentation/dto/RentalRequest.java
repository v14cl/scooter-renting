package com.vlad.scooterrental.presentation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentalRequest(
        @NotNull UUID scooterId,
        @NotNull @Future LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {
}
