package com.vlad.scooterrental.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vlad.scooterrental.analytics.infrastructure.persistence.InMemoryAnalyticsProjectionRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class AnalyticsIntegrationTest extends IntegrationTestSupport {
  @Autowired private InMemoryAnalyticsProjectionRepository analyticsProjectionRepository;

  @BeforeEach
  void clearAnalytics() {
    analyticsProjectionRepository.clear();
  }

  @Test
  void dashboardShouldEventuallyReflectCoreEvents() throws Exception {
    String adminToken = loginAdmin();
    Map<String, Object> scooter = createScooter(adminToken, "SCT-AN-1");
    String customerToken = registerCustomer("analytics-rider@example.com");

    mockMvc
        .perform(
            post("/api/rentals")
                .header("Authorization", "Bearer " + customerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    json(
                        rentalPayload(
                            scooter.get("id").toString(),
                            LocalDateTime.now().plusHours(1),
                            LocalDateTime.now().plusHours(2)))))
        .andExpect(status().isCreated());

    Map<String, Object> dashboard = waitForDashboard(customerToken);

    assertTrue(((Number) dashboard.get("totalUsersRegistered")).longValue() >= 1);
    assertTrue(((Number) dashboard.get("totalScootersCreated")).longValue() >= 1);
    assertTrue(((Number) dashboard.get("totalRentalsCreated")).longValue() >= 1);
    assertFalse(dashboard.containsKey("passwordHash"));
    assertFalse(dashboard.containsKey("renterId"));
    assertFalse(dashboard.containsKey("scooterId"));
  }

  private Map<String, Object> waitForDashboard(String token) throws Exception {
    AssertionError lastError = null;
    for (int attempt = 0; attempt < 20; attempt++) {
      String response =
          mockMvc
              .perform(get("/api/analytics/dashboard").header("Authorization", "Bearer " + token))
              .andExpect(status().isOk())
              .andReturn()
              .getResponse()
              .getContentAsString();
      Map<String, Object> dashboard = read(response);
      try {
        assertTrue(((Number) dashboard.get("totalRentalsCreated")).longValue() >= 1);
        return dashboard;
      } catch (AssertionError error) {
        lastError = error;
        Thread.sleep(100);
      }
    }
    throw lastError;
  }
}
