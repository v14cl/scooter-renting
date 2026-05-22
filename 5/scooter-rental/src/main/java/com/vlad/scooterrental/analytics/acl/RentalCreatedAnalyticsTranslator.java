package com.vlad.scooterrental.analytics.acl;

import com.vlad.scooterrental.analytics.domain.model.RentalActivity;
import com.vlad.scooterrental.analytics.domain.model.RentalActivityType;
import com.vlad.scooterrental.core.api.event.RentalCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class RentalCreatedAnalyticsTranslator {
  public RentalActivity translate(RentalCreatedEvent event) {
    return new RentalActivity(
        event.eventId(),
        event.rentalId(),
        event.renterId(),
        event.scooterId(),
        event.startTime(),
        event.endTime(),
        RentalActivityType.CREATED,
        event.occurredAt());
  }
}
