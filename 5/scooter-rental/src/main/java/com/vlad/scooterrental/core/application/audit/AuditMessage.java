package com.vlad.scooterrental.core.application.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditMessage(
    UUID eventId,
    AuditAction action,
    String actorId,
    String entityType,
    String entityId,
    Instant occurredAt,
    String details) {
  public AuditMessage {
    if (eventId == null) {
      throw new IllegalArgumentException("Audit event id must be provided");
    }
    if (action == null) {
      throw new IllegalArgumentException("Audit action must be provided");
    }
    if (actorId == null || actorId.isBlank()) {
      throw new IllegalArgumentException("Audit actor id must be provided");
    }
    if (entityType == null || entityType.isBlank()) {
      throw new IllegalArgumentException("Audit entity type must be provided");
    }
    if (entityId == null || entityId.isBlank()) {
      throw new IllegalArgumentException("Audit entity id must be provided");
    }
    if (occurredAt == null) {
      throw new IllegalArgumentException("Audit occurrence time must be provided");
    }
    details = details == null ? "" : details.trim();
  }
}
