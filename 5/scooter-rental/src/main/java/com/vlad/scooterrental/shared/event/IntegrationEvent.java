package com.vlad.scooterrental.shared.event;

import java.time.Instant;
import java.util.UUID;

public interface IntegrationEvent {
  UUID eventId();

  Instant occurredAt();
}
