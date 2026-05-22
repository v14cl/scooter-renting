package com.vlad.scooterrental.analytics.acl;

import com.vlad.scooterrental.analytics.domain.model.ScooterActivity;
import com.vlad.scooterrental.analytics.domain.model.ScooterActivityType;
import com.vlad.scooterrental.core.api.event.ScooterCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class ScooterCreatedAnalyticsTranslator {
  public ScooterActivity translate(ScooterCreatedEvent event) {
    return new ScooterActivity(
        event.eventId(),
        event.scooterId(),
        event.status(),
        ScooterActivityType.CREATED,
        event.occurredAt());
  }
}
