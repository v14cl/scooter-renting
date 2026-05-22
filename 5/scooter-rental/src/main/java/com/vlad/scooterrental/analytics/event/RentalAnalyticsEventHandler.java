package com.vlad.scooterrental.analytics.event;

import com.vlad.scooterrental.analytics.acl.CoreEventToAnalyticsTranslator;
import com.vlad.scooterrental.analytics.application.projection.AnalyticsProjectionRepository;
import com.vlad.scooterrental.core.api.event.RentalCreatedEvent;
import com.vlad.scooterrental.core.api.event.RentalDeletedEvent;
import com.vlad.scooterrental.core.api.event.RentalUpdatedEvent;
import com.vlad.scooterrental.shared.event.EventHandler;
import com.vlad.scooterrental.shared.event.IntegrationEvent;
import org.springframework.stereotype.Component;

@Component
public class RentalAnalyticsEventHandler implements EventHandler<IntegrationEvent> {
  private final CoreEventToAnalyticsTranslator translator;
  private final AnalyticsProjectionRepository projectionRepository;

  public RentalAnalyticsEventHandler(
      CoreEventToAnalyticsTranslator translator,
      AnalyticsProjectionRepository projectionRepository) {
    this.translator = translator;
    this.projectionRepository = projectionRepository;
  }

  @Override
  public void handle(IntegrationEvent event) {
    if (event instanceof RentalCreatedEvent rentalCreated) {
      projectionRepository.apply(translator.translate(rentalCreated));
    } else if (event instanceof RentalUpdatedEvent rentalUpdated) {
      projectionRepository.apply(translator.translate(rentalUpdated));
    } else if (event instanceof RentalDeletedEvent rentalDeleted) {
      projectionRepository.apply(translator.translate(rentalDeleted));
    }
  }

  @Override
  public Class<IntegrationEvent> eventType() {
    return IntegrationEvent.class;
  }
}
