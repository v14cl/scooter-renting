package com.vlad.scooterrental.analytics.acl;

import com.vlad.scooterrental.analytics.domain.model.RentalActivity;
import com.vlad.scooterrental.analytics.domain.model.RentalActivityType;
import com.vlad.scooterrental.core.api.event.RentalUpdatedEvent;
import org.springframework.stereotype.Component;

@Component
public class RentalUpdatedAnalyticsTranslator {
  public RentalActivity translate(RentalUpdatedEvent event) {
    return new RentalActivity(
        event.eventId(),
        event.rentalId(),
        event.renterId(),
        event.scooterId(),
        event.startTime(),
        event.endTime(),
        RentalActivityType.UPDATED,
        event.occurredAt());
  }
}
