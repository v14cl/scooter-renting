package com.vlad.scooterrental.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AuthIntegrationTest extends IntegrationTestSupport {

  @Test
  void shouldRegisterUserAndReturnToken() throws Exception {
    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    json(
                        Map.of(
                            "fullName", "Alice Rider",
                            "email", "alice@example.com",
                            "password", "password1"))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").isNotEmpty())
        .andExpect(jsonPath("$.user.email").value("alice@example.com"));
  }

  @Test
  void shouldReturnConflictForDuplicateRegistration() throws Exception {
    registerCustomer("duplicate@example.com");

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    json(
                        Map.of(
                            "fullName", "Another User",
                            "email", "duplicate@example.com",
                            "password", "password1"))))
        .andExpect(status().isConflict());
  }

  @Test
  void shouldReturnUnauthorizedForProtectedEndpointWithoutToken() throws Exception {
    mockMvc.perform(get("/api/users/me")).andExpect(status().isUnauthorized());
  }
}
