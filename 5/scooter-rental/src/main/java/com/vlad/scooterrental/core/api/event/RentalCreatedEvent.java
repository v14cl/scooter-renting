package com.vlad.scooterrental.core.api.event;

import com.vlad.scooterrental.shared.event.IntegrationEvent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record RentalCreatedEvent(
    UUID eventId,
    UUID rentalId,
    UUID scooterId,
    UUID renterId,
    UUID actorId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Instant occurredAt)
    implements IntegrationEvent {}
