package com.vlad.scooterrental.core.api.event;

import com.vlad.scooterrental.shared.event.IntegrationEvent;
import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
    UUID eventId, UUID userId, String email, String fullName, Instant occurredAt)
    implements IntegrationEvent {}
