package com.vlad.scooterrental.analytics.domain.model;

import java.time.Instant;
import java.util.UUID;

public record UserRegistrationActivity(
    UUID eventId, UUID userId, String email, String fullName, Instant occurredAt) {}
