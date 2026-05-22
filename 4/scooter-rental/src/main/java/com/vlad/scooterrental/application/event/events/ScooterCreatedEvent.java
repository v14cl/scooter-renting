package com.vlad.scooterrental.application.event.events;

import com.vlad.scooterrental.application.event.IntegrationEvent;
import java.time.Instant;
import java.util.UUID;

public record ScooterCreatedEvent(
    UUID eventId, UUID scooterId, String code, String model, String actorId, Instant occurredAt)
    implements IntegrationEvent {}
