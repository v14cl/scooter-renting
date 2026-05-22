package com.vlad.scooterrental.application.event;

import java.time.Instant;
import java.util.UUID;

public interface IntegrationEvent {
  UUID eventId();

  Instant occurredAt();
}
