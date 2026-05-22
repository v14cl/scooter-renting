package com.vlad.scooterrental.analytics.presentation;

import com.vlad.scooterrental.analytics.api.AnalyticsDashboardView;
import com.vlad.scooterrental.analytics.api.AnalyticsModuleApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
  private final AnalyticsModuleApi analyticsModuleApi;

  public AnalyticsController(AnalyticsModuleApi analyticsModuleApi) {
    this.analyticsModuleApi = analyticsModuleApi;
  }

  @GetMapping("/dashboard")
  public AnalyticsDashboardView dashboard() {
    return analyticsModuleApi.dashboard();
  }
}
