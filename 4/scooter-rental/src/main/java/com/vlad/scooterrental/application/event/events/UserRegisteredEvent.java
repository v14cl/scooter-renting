package com.vlad.scooterrental.application.event.events;

import com.vlad.scooterrental.application.event.IntegrationEvent;
import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
    UUID eventId, UUID userId, String email, String fullName, Instant occurredAt)
    implements IntegrationEvent {}
