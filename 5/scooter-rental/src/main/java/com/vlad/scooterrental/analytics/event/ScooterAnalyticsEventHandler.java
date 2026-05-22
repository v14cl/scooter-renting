package com.vlad.scooterrental.analytics.event;

import com.vlad.scooterrental.analytics.acl.CoreEventToAnalyticsTranslator;
import com.vlad.scooterrental.analytics.application.projection.AnalyticsProjectionRepository;
import com.vlad.scooterrental.core.api.event.ScooterCreatedEvent;
import com.vlad.scooterrental.core.api.event.ScooterDeletedEvent;
import com.vlad.scooterrental.core.api.event.ScooterUpdatedEvent;
import com.vlad.scooterrental.shared.event.EventHandler;
import com.vlad.scooterrental.shared.event.IntegrationEvent;
import org.springframework.stereotype.Component;

@Component
public class ScooterAnalyticsEventHandler implements EventHandler<IntegrationEvent> {
  private final CoreEventToAnalyticsTranslator translator;
  private final AnalyticsProjectionRepository projectionRepository;

  public ScooterAnalyticsEventHandler(
      CoreEventToAnalyticsTranslator translator,
      AnalyticsProjectionRepository projectionRepository) {
    this.translator = translator;
    this.projectionRepository = projectionRepository;
  }

  @Override
  public void handle(IntegrationEvent event) {
    if (event instanceof ScooterCreatedEvent scooterCreated) {
      projectionRepository.apply(translator.translate(scooterCreated));
    } else if (event instanceof ScooterUpdatedEvent scooterUpdated) {
      projectionRepository.apply(translator.translate(scooterUpdated));
    } else if (event instanceof ScooterDeletedEvent scooterDeleted) {
      projectionRepository.apply(translator.translate(scooterDeleted));
    }
  }

  @Override
  public Class<IntegrationEvent> eventType() {
    return IntegrationEvent.class;
  }
}
