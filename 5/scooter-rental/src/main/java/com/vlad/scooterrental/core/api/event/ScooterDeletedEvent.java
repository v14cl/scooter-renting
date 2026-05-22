package com.vlad.scooterrental.core.api.event;

import com.vlad.scooterrental.shared.event.IntegrationEvent;
import java.time.Instant;
import java.util.UUID;

public record ScooterDeletedEvent(
    UUID eventId, UUID scooterId, String code, String actorId, Instant occurredAt)
    implements IntegrationEvent {}
