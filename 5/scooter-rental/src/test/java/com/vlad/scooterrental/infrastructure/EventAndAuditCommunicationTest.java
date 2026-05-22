package com.vlad.scooterrental.core.infrastructure;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vlad.scooterrental.core.application.audit.AuditAction;
import com.vlad.scooterrental.shared.event.EventHandler;
import com.vlad.scooterrental.shared.event.IntegrationEvent;
import com.vlad.scooterrental.core.api.event.RentalCreatedEvent;
import com.vlad.scooterrental.core.infrastructure.audit.AuditIntegrationEventHandler;
import com.vlad.scooterrental.core.infrastructure.audit.InMemoryAuditStore;
import com.vlad.scooterrental.core.infrastructure.audit.LoggingAuditRecorder;
import com.vlad.scooterrental.core.infrastructure.event.AsyncInMemoryEventBus;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class EventAndAuditCommunicationTest {

  @Test
  void asyncEventBusShouldDispatchEventToMatchingSubscriber() {
    AtomicBoolean handled = new AtomicBoolean(false);
    EventHandler<RentalCreatedEvent> handler =
        new EventHandler<>() {
          @Override
          public void handle(RentalCreatedEvent event) {
            handled.set(true);
          }

          @Override
          public Class<RentalCreatedEvent> eventType() {
            return RentalCreatedEvent.class;
          }
        };
    AsyncInMemoryEventBus eventBus = new AsyncInMemoryEventBus(List.of(handler), Runnable::run);

    eventBus.publish(newRentalCreatedEvent(UUID.randomUUID()));

    assertTrue(handled.get());
  }

  @Test
  void asyncEventBusShouldNotFailPublisherWhenSubscriberFails() {
    EventHandler<RentalCreatedEvent> failingHandler =
        new EventHandler<>() {
          @Override
          public void handle(RentalCreatedEvent event) {
            throw new IllegalStateException("subscriber failed");
          }

          @Override
          public Class<RentalCreatedEvent> eventType() {
            return RentalCreatedEvent.class;
          }
        };
    AsyncInMemoryEventBus eventBus =
        new AsyncInMemoryEventBus(List.of(failingHandler), Runnable::run);

    assertDoesNotThrow(() -> eventBus.publish(newRentalCreatedEvent(UUID.randomUUID())));
  }

  @Test
  void auditSubscriberShouldIgnoreDuplicateEventId() {
    InMemoryAuditStore store = new InMemoryAuditStore();
    AuditIntegrationEventHandler handler =
        new AuditIntegrationEventHandler(new LoggingAuditRecorder(store));
    UUID eventId = UUID.randomUUID();
    RentalCreatedEvent event = newRentalCreatedEvent(eventId);

    handler.handle(event);
    handler.handle(event);

    assertEquals(1, store.records().size());
    assertEquals(AuditAction.RENTAL_CREATED, store.records().get(0).action());
    assertEquals(eventId, store.records().get(0).eventId());
  }

  private static RentalCreatedEvent newRentalCreatedEvent(UUID eventId) {
    UUID renterId = UUID.randomUUID();
    return new RentalCreatedEvent(
        eventId,
        UUID.randomUUID(),
        UUID.randomUUID(),
        renterId,
        renterId,
        LocalDateTime.of(2026, 1, 1, 11, 0),
        LocalDateTime.of(2026, 1, 1, 12, 0),
        Instant.parse("2026-01-01T10:00:00Z"));
  }
}
