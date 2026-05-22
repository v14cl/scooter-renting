package com.vlad.scooterrental.analytics.domain.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record RentalActivity(
    UUID eventId,
    UUID rentalId,
    UUID renterId,
    UUID scooterId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    RentalActivityType activityType,
    Instant occurredAt) {}
