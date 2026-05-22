package com.vlad.scooterrental.application.event.events;

import com.vlad.scooterrental.application.event.IntegrationEvent;
import java.time.Instant;
import java.util.UUID;

public record ScooterDeletedEvent(
    UUID eventId, UUID scooterId, String code, String actorId, Instant occurredAt)
    implements IntegrationEvent {}
