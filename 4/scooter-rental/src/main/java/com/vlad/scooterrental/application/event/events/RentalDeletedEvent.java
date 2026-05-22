package com.vlad.scooterrental.application.event.events;

import com.vlad.scooterrental.application.event.IntegrationEvent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record RentalDeletedEvent(
    UUID eventId,
    UUID rentalId,
    UUID scooterId,
    UUID renterId,
    UUID actorId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Instant occurredAt)
    implements IntegrationEvent {}
