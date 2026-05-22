package com.vlad.scooterrental.analytics.acl;

import com.vlad.scooterrental.analytics.domain.model.RentalActivity;
import com.vlad.scooterrental.analytics.domain.model.ScooterActivity;
import com.vlad.scooterrental.analytics.domain.model.UserRegistrationActivity;
import com.vlad.scooterrental.core.api.event.RentalCreatedEvent;
import com.vlad.scooterrental.core.api.event.RentalDeletedEvent;
import com.vlad.scooterrental.core.api.event.RentalUpdatedEvent;
import com.vlad.scooterrental.core.api.event.ScooterCreatedEvent;
import com.vlad.scooterrental.core.api.event.ScooterDeletedEvent;
import com.vlad.scooterrental.core.api.event.ScooterUpdatedEvent;
import com.vlad.scooterrental.core.api.event.UserRegisteredEvent;
import org.springframework.stereotype.Component;

@Component
public class CoreEventToAnalyticsTranslator {
  private final UserRegisteredAnalyticsTranslator userRegisteredTranslator;
  private final ScooterCreatedAnalyticsTranslator scooterCreatedTranslator;
  private final ScooterUpdatedAnalyticsTranslator scooterUpdatedTranslator;
  private final ScooterDeletedAnalyticsTranslator scooterDeletedTranslator;
  private final RentalCreatedAnalyticsTranslator rentalCreatedTranslator;
  private final RentalUpdatedAnalyticsTranslator rentalUpdatedTranslator;
  private final RentalDeletedAnalyticsTranslator rentalDeletedTranslator;

  public CoreEventToAnalyticsTranslator(
      UserRegisteredAnalyticsTranslator userRegisteredTranslator,
      ScooterCreatedAnalyticsTranslator scooterCreatedTranslator,
      ScooterUpdatedAnalyticsTranslator scooterUpdatedTranslator,
      ScooterDeletedAnalyticsTranslator scooterDeletedTranslator,
      RentalCreatedAnalyticsTranslator rentalCreatedTranslator,
      RentalUpdatedAnalyticsTranslator rentalUpdatedTranslator,
      RentalDeletedAnalyticsTranslator rentalDeletedTranslator) {
    this.userRegisteredTranslator = userRegisteredTranslator;
    this.scooterCreatedTranslator = scooterCreatedTranslator;
    this.scooterUpdatedTranslator = scooterUpdatedTranslator;
    this.scooterDeletedTranslator = scooterDeletedTranslator;
    this.rentalCreatedTranslator = rentalCreatedTranslator;
    this.rentalUpdatedTranslator = rentalUpdatedTranslator;
    this.rentalDeletedTranslator = rentalDeletedTranslator;
  }

  public UserRegistrationActivity translate(UserRegisteredEvent event) {
    return userRegisteredTranslator.translate(event);
  }

  public ScooterActivity translate(ScooterCreatedEvent event) {
    return scooterCreatedTranslator.translate(event);
  }

  public ScooterActivity translate(ScooterUpdatedEvent event) {
    return scooterUpdatedTranslator.translate(event);
  }

  public ScooterActivity translate(ScooterDeletedEvent event) {
    return scooterDeletedTranslator.translate(event);
  }

  public RentalActivity translate(RentalCreatedEvent event) {
    return rentalCreatedTranslator.translate(event);
  }

  public RentalActivity translate(RentalUpdatedEvent event) {
    return rentalUpdatedTranslator.translate(event);
  }

  public RentalActivity translate(RentalDeletedEvent event) {
    return rentalDeletedTranslator.translate(event);
  }
}
