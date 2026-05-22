package com.vlad.scooterrental.domain.model;

import com.vlad.scooterrental.domain.value.RentalPeriod;

import java.util.UUID;

public record Rental(
        UUID id,
        UUID renterId,
        UUID scooterId,
        RentalPeriod period
) {
}
