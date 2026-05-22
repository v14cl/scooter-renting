package com.vlad.scooterrental.analytics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.vlad.scooterrental.analytics.acl.CoreEventToAnalyticsTranslator;
import com.vlad.scooterrental.analytics.acl.RentalCreatedAnalyticsTranslator;
import com.vlad.scooterrental.analytics.acl.RentalDeletedAnalyticsTranslator;
import com.vlad.scooterrental.analytics.acl.RentalUpdatedAnalyticsTranslator;
import com.vlad.scooterrental.analytics.acl.ScooterCreatedAnalyticsTranslator;
import com.vlad.scooterrental.analytics.acl.ScooterDeletedAnalyticsTranslator;
import com.vlad.scooterrental.analytics.acl.ScooterUpdatedAnalyticsTranslator;
import com.vlad.scooterrental.analytics.acl.UserRegisteredAnalyticsTranslator;
import com.vlad.scooterrental.analytics.event.RentalAnalyticsEventHandler;
import com.vlad.scooterrental.analytics.event.ScooterAnalyticsEventHandler;
import com.vlad.scooterrental.analytics.event.UserRegisteredAnalyticsEventHandler;
import com.vlad.scooterrental.analytics.infrastructure.persistence.InMemoryAnalyticsProjectionRepository;
import com.vlad.scooterrental.core.api.event.RentalCreatedEvent;
import com.vlad.scooterrental.core.api.event.ScooterCreatedEvent;
import com.vlad.scooterrental.core.api.event.UserRegisteredEvent;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AnalyticsEventHandlerTest {
  @Test
  void shouldUpdateDashboardProjectionAndIgnoreDuplicateEvents() {
    InMemoryAnalyticsProjectionRepository repository =
        new InMemoryAnalyticsProjectionRepository(
            Clock.fixed(Instant.parse("2026-05-22T10:00:00Z"), ZoneOffset.UTC));
    CoreEventToAnalyticsTranslator translator = translator();
    UserRegisteredAnalyticsEventHandler userHandler =
        new UserRegisteredAnalyticsEventHandler(translator, repository);
    ScooterAnalyticsEventHandler scooterHandler =
        new ScooterAnalyticsEventHandler(translator, repository);
    RentalAnalyticsEventHandler rentalHandler = new RentalAnalyticsEventHandler(translator, repository);

    UserRegisteredEvent userEvent =
        new UserRegisteredEvent(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "rider@example.com",
            "Rider User",
            Instant.parse("2026-05-22T10:00:00Z"));
    ScooterCreatedEvent scooterEvent =
        new ScooterCreatedEvent(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "SCT-777",
            "Ninebot",
            "AVAILABLE",
            "system",
            Instant.parse("2026-05-22T10:01:00Z"));
    RentalCreatedEvent rentalEvent =
        new RentalCreatedEvent(
            UUID.randomUUID(),
            UUID.randomUUID(),
            scooterEvent.scooterId(),
            userEvent.userId(),
            userEvent.userId(),
            LocalDateTime.of(2026, 5, 22, 11, 0),
            LocalDateTime.of(2026, 5, 22, 12, 0),
            Instant.parse("2026-05-22T10:02:00Z"));

    userHandler.handle(userEvent);
    userHandler.handle(userEvent);
    scooterHandler.handle(scooterEvent);
    scooterHandler.handle(scooterEvent);
    rentalHandler.handle(rentalEvent);
    rentalHandler.handle(rentalEvent);

    var dashboard = repository.dashboard();
    assertEquals(1, dashboard.totalUsersRegistered());
    assertEquals(1, dashboard.totalScootersCreated());
    assertEquals(1, dashboard.totalRentalsCreated());
    assertEquals(1, dashboard.activeRentalsCount());
    assertEquals(1, dashboard.scootersByStatus().get("AVAILABLE"));
    assertEquals(1, dashboard.rentalsByDay().get(rentalEvent.startTime().toLocalDate()));
  }

  private CoreEventToAnalyticsTranslator translator() {
    return new CoreEventToAnalyticsTranslator(
        new UserRegisteredAnalyticsTranslator(),
        new ScooterCreatedAnalyticsTranslator(),
        new ScooterUpdatedAnalyticsTranslator(),
        new ScooterDeletedAnalyticsTranslator(),
        new RentalCreatedAnalyticsTranslator(),
        new RentalUpdatedAnalyticsTranslator(),
        new RentalDeletedAnalyticsTranslator());
  }
}
