package com.vlad.scooterrental.core.api.event;

import com.vlad.scooterrental.shared.event.IntegrationEvent;
import java.time.Instant;
import java.util.UUID;

public record ScooterUpdatedEvent(
    UUID eventId,
    UUID scooterId,
    String code,
    String model,
    String status,
    String actorId,
    Instant occurredAt)
    implements IntegrationEvent {}
