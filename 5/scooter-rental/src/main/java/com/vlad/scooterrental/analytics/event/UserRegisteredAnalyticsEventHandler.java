package com.vlad.scooterrental.analytics.event;

import com.vlad.scooterrental.analytics.acl.CoreEventToAnalyticsTranslator;
import com.vlad.scooterrental.analytics.application.projection.AnalyticsProjectionRepository;
import com.vlad.scooterrental.core.api.event.UserRegisteredEvent;
import com.vlad.scooterrental.shared.event.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredAnalyticsEventHandler implements EventHandler<UserRegisteredEvent> {
  private final CoreEventToAnalyticsTranslator translator;
  private final AnalyticsProjectionRepository projectionRepository;

  public UserRegisteredAnalyticsEventHandler(
      CoreEventToAnalyticsTranslator translator,
      AnalyticsProjectionRepository projectionRepository) {
    this.translator = translator;
    this.projectionRepository = projectionRepository;
  }

  @Override
  public void handle(UserRegisteredEvent event) {
    projectionRepository.apply(translator.translate(event));
  }

  @Override
  public Class<UserRegisteredEvent> eventType() {
    return UserRegisteredEvent.class;
  }
}
