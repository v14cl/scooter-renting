package com.vlad.scooterrental.analytics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.vlad.scooterrental.analytics.acl.RentalCreatedAnalyticsTranslator;
import com.vlad.scooterrental.analytics.acl.UserRegisteredAnalyticsTranslator;
import com.vlad.scooterrental.analytics.domain.model.RentalActivityType;
import com.vlad.scooterrental.core.api.event.RentalCreatedEvent;
import com.vlad.scooterrental.core.api.event.UserRegisteredEvent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AnalyticsAclTest {
  @Test
  void shouldTranslateRentalCreatedEventToAnalyticsInternalModel() {
    UUID eventId = UUID.randomUUID();
    UUID rentalId = UUID.randomUUID();
    UUID scooterId = UUID.randomUUID();
    UUID renterId = UUID.randomUUID();
    RentalCreatedEvent event =
        new RentalCreatedEvent(
            eventId,
            rentalId,
            scooterId,
            renterId,
            renterId,
            LocalDateTime.of(2026, 6, 1, 10, 0),
            LocalDateTime.of(2026, 6, 1, 11, 0),
            Instant.parse("2026-06-01T09:00:00Z"));

    var activity = new RentalCreatedAnalyticsTranslator().translate(event);

    assertEquals(eventId, activity.eventId());
    assertEquals(rentalId, activity.rentalId());
    assertEquals(scooterId, activity.scooterId());
    assertEquals(renterId, activity.renterId());
    assertEquals(RentalActivityType.CREATED, activity.activityType());
  }

  @Test
  void shouldTranslateUserRegisteredEventToAnalyticsInternalModel() {
    UUID eventId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UserRegisteredEvent event =
        new UserRegisteredEvent(
            eventId,
            userId,
            "rider@example.com",
            "Rider User",
            Instant.parse("2026-06-01T09:00:00Z"));

    var activity = new UserRegisteredAnalyticsTranslator().translate(event);

    assertEquals(eventId, activity.eventId());
    assertEquals(userId, activity.userId());
    assertEquals("rider@example.com", activity.email());
    assertEquals("Rider User", activity.fullName());
  }
}
