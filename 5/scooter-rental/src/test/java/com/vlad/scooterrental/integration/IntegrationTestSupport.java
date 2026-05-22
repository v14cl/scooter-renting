package com.vlad.scooterrental.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.scooterrental.core.infrastructure.persistence.jpa.RentalJpaRepository;
import com.vlad.scooterrental.core.infrastructure.persistence.jpa.ScooterJpaRepository;
import com.vlad.scooterrental.core.infrastructure.persistence.jpa.UserJpaRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
abstract class IntegrationTestSupport {

  @Autowired protected MockMvc mockMvc;

  protected final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired protected RentalJpaRepository rentalJpaRepository;

  @Autowired protected ScooterJpaRepository scooterJpaRepository;

  @Autowired protected UserJpaRepository userJpaRepository;

  @BeforeEach
  void cleanDatabase() {
    rentalJpaRepository.deleteAll();
    scooterJpaRepository.deleteAll();
    userJpaRepository.findAll().stream()
        .filter(user -> !"admin@scooter.local".equals(user.getEmail()))
        .forEach(userJpaRepository::delete);
  }

  protected String registerCustomer(String email) throws Exception {
    String response =
        mockMvc
            .perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        json(
                            Map.of(
                                "fullName", "Test Customer",
                                "email", email,
                                "password", "password1"))))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return (String) read(response).get("token");
  }

  protected String loginAdmin() throws Exception {
    String response =
        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        json(
                            Map.of(
                                "email", "admin@scooter.local",
                                "password", "admin12345"))))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return (String) read(response).get("token");
  }

  protected Map<String, Object> createScooter(String adminToken, String code) throws Exception {
    String response =
        mockMvc
            .perform(
                post("/api/scooters")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        json(
                            Map.of(
                                "code",
                                code,
                                "model",
                                "Ninebot Max",
                                "status",
                                "AVAILABLE",
                                "batteryLevel",
                                85,
                                "pricePerMinute",
                                2.50))))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return read(response);
  }

  protected String json(Object value) throws Exception {
    return objectMapper.writeValueAsString(value);
  }

  protected Map<String, Object> read(String value) throws Exception {
    return objectMapper.readValue(value, Map.class);
  }

  protected Map<String, Object> rentalPayload(
      String scooterId, LocalDateTime startTime, LocalDateTime endTime) {
    return Map.of(
        "scooterId", scooterId,
        "startTime", startTime.toString(),
        "endTime", endTime.toString());
  }
}
