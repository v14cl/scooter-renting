package com.vlad.scooterrental.analytics.acl;

import com.vlad.scooterrental.analytics.domain.model.ScooterActivity;
import com.vlad.scooterrental.analytics.domain.model.ScooterActivityType;
import com.vlad.scooterrental.core.api.event.ScooterDeletedEvent;
import org.springframework.stereotype.Component;

@Component
public class ScooterDeletedAnalyticsTranslator {
  public ScooterActivity translate(ScooterDeletedEvent event) {
    return new ScooterActivity(
        event.eventId(),
        event.scooterId(),
        "DELETED",
        ScooterActivityType.DELETED,
        event.occurredAt());
  }
}
