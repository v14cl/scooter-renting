package com.vlad.scooterrental.infrastructure.audit;

import com.vlad.scooterrental.application.audit.AuditAction;
import com.vlad.scooterrental.application.audit.AuditMessage;
import com.vlad.scooterrental.application.audit.AuditRecorder;
import com.vlad.scooterrental.application.event.EventHandler;
import com.vlad.scooterrental.application.event.IntegrationEvent;
import com.vlad.scooterrental.application.event.events.RentalCreatedEvent;
import com.vlad.scooterrental.application.event.events.RentalDeletedEvent;
import com.vlad.scooterrental.application.event.events.RentalUpdatedEvent;
import com.vlad.scooterrental.application.event.events.ScooterCreatedEvent;
import com.vlad.scooterrental.application.event.events.ScooterDeletedEvent;
import com.vlad.scooterrental.application.event.events.ScooterUpdatedEvent;
import com.vlad.scooterrental.application.event.events.UserRegisteredEvent;
import org.springframework.stereotype.Component;

@Component
public class AuditIntegrationEventHandler implements EventHandler<IntegrationEvent> {
  private final AuditRecorder auditRecorder;

  public AuditIntegrationEventHandler(AuditRecorder auditRecorder) {
    this.auditRecorder = auditRecorder;
  }

  @Override
  public void handle(IntegrationEvent event) {
    AuditMessage message = toAuditMessage(event);
    if (message != null) {
      auditRecorder.record(message);
    }
  }

  @Override
  public Class<IntegrationEvent> eventType() {
    return IntegrationEvent.class;
  }

  private AuditMessage toAuditMessage(IntegrationEvent event) {
    if (event instanceof UserRegisteredEvent userRegistered) {
      return new AuditMessage(
          userRegistered.eventId(),
          AuditAction.USER_REGISTERED,
          userRegistered.userId().toString(),
          "User",
          userRegistered.userId().toString(),
          userRegistered.occurredAt(),
          "Registered user " + userRegistered.email());
    }
    if (event instanceof ScooterCreatedEvent scooterCreated) {
      return new AuditMessage(
          scooterCreated.eventId(),
          AuditAction.SCOOTER_CREATED,
          scooterCreated.actorId(),
          "Scooter",
          scooterCreated.scooterId().toString(),
          scooterCreated.occurredAt(),
          "Created scooter " + scooterCreated.code());
    }
    if (event instanceof ScooterUpdatedEvent scooterUpdated) {
      return new AuditMessage(
          scooterUpdated.eventId(),
          AuditAction.SCOOTER_UPDATED,
          scooterUpdated.actorId(),
          "Scooter",
          scooterUpdated.scooterId().toString(),
          scooterUpdated.occurredAt(),
          "Updated scooter " + scooterUpdated.code());
    }
    if (event instanceof ScooterDeletedEvent scooterDeleted) {
      return new AuditMessage(
          scooterDeleted.eventId(),
          AuditAction.SCOOTER_DELETED,
          scooterDeleted.actorId(),
          "Scooter",
          scooterDeleted.scooterId().toString(),
          scooterDeleted.occurredAt(),
          "Deleted scooter " + scooterDeleted.code());
    }
    if (event instanceof RentalCreatedEvent rentalCreated) {
      return new AuditMessage(
          rentalCreated.eventId(),
          AuditAction.RENTAL_CREATED,
          rentalCreated.actorId().toString(),
          "Rental",
          rentalCreated.rentalId().toString(),
          rentalCreated.occurredAt(),
          "Created rental for scooter " + rentalCreated.scooterId());
    }
    if (event instanceof RentalUpdatedEvent rentalUpdated) {
      return new AuditMessage(
          rentalUpdated.eventId(),
          AuditAction.RENTAL_UPDATED,
          rentalUpdated.actorId().toString(),
          "Rental",
          rentalUpdated.rentalId().toString(),
          rentalUpdated.occurredAt(),
          "Updated rental for scooter " + rentalUpdated.scooterId());
    }
    if (event instanceof RentalDeletedEvent rentalDeleted) {
      return new AuditMessage(
          rentalDeleted.eventId(),
          AuditAction.RENTAL_DELETED,
          rentalDeleted.actorId().toString(),
          "Rental",
          rentalDeleted.rentalId().toString(),
          rentalDeleted.occurredAt(),
          "Deleted rental for scooter " + rentalDeleted.scooterId());
    }
    return null;
  }
}
