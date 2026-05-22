package com.vlad.scooterrental.analytics.domain.model;

import java.time.Instant;
import java.util.UUID;

public record ScooterActivity(
    UUID eventId,
    UUID scooterId,
    String status,
    ScooterActivityType activityType,
    Instant occurredAt) {}
