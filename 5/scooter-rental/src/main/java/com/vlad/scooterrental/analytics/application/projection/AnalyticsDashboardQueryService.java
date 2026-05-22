package com.vlad.scooterrental.analytics.application.projection;

import com.vlad.scooterrental.analytics.api.AnalyticsDashboardView;
import com.vlad.scooterrental.analytics.api.AnalyticsModuleApi;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsDashboardQueryService implements AnalyticsModuleApi {
  private final AnalyticsProjectionRepository projectionRepository;

  public AnalyticsDashboardQueryService(AnalyticsProjectionRepository projectionRepository) {
    this.projectionRepository = projectionRepository;
  }

  @Override
  public AnalyticsDashboardView dashboard() {
    return projectionRepository.dashboard();
  }
}
