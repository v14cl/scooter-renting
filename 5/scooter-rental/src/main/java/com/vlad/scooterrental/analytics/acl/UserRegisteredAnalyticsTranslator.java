package com.vlad.scooterrental.analytics.acl;

import com.vlad.scooterrental.analytics.domain.model.UserRegistrationActivity;
import com.vlad.scooterrental.core.api.event.UserRegisteredEvent;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredAnalyticsTranslator {
  public UserRegistrationActivity translate(UserRegisteredEvent event) {
    return new UserRegistrationActivity(
        event.eventId(), event.userId(), event.email(), event.fullName(), event.occurredAt());
  }
}
