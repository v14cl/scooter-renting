package com.vlad.scooterrental.analytics.application.projection;

import com.vlad.scooterrental.analytics.api.AnalyticsDashboardView;
import com.vlad.scooterrental.analytics.domain.model.RentalActivity;
import com.vlad.scooterrental.analytics.domain.model.ScooterActivity;
import com.vlad.scooterrental.analytics.domain.model.UserRegistrationActivity;

public interface AnalyticsProjectionRepository {
  void apply(UserRegistrationActivity activity);

  void apply(ScooterActivity activity);

  void apply(RentalActivity activity);

  AnalyticsDashboardView dashboard();
}
