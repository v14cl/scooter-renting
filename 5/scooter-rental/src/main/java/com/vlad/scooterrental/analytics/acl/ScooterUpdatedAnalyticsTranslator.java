package com.vlad.scooterrental.analytics.acl;

import com.vlad.scooterrental.analytics.domain.model.ScooterActivity;
import com.vlad.scooterrental.analytics.domain.model.ScooterActivityType;
import com.vlad.scooterrental.core.api.event.ScooterUpdatedEvent;
import org.springframework.stereotype.Component;

@Component
public class ScooterUpdatedAnalyticsTranslator {
  public ScooterActivity translate(ScooterUpdatedEvent event) {
    return new ScooterActivity(
        event.eventId(),
        event.scooterId(),
        event.status(),
        ScooterActivityType.UPDATED,
        event.occurredAt());
  }
}
