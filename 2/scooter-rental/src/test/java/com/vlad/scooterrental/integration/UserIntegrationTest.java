package com.vlad.scooterrental.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class UserIntegrationTest extends IntegrationTestSupport {

  @Test
  void adminShouldManageUsersCrud() throws Exception {
    String adminToken = loginAdmin();

    String createdUser =
        mockMvc
            .perform(
                post("/api/users")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        json(
                            Map.of(
                                "fullName", "Managed User",
                                "email", "managed@example.com",
                                "password", "password1",
                                "role", "CUSTOMER"))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("managed@example.com"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    String userId = (String) read(createdUser).get("id");

    mockMvc
        .perform(get("/api/users").header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.email=='managed@example.com')]").exists());

    mockMvc
        .perform(get("/api/users/{userId}", userId).header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.fullName").value("Managed User"));

    mockMvc
        .perform(
            put("/api/users/{userId}", userId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    json(
                        Map.of(
                            "fullName", "Updated Managed User",
                            "email", "managed-updated@example.com",
                            "password", "password2",
                            "role", "ADMIN"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.role").value("ADMIN"));

    mockMvc
        .perform(
            delete("/api/users/{userId}", userId).header("Authorization", "Bearer " + adminToken))
        .andExpect(status().isNoContent());
  }

  @Test
  void customerShouldNotAccessAdminUserCrudEndpoints() throws Exception {
    String customerToken = registerCustomer("plain@example.com");

    mockMvc
        .perform(get("/api/users").header("Authorization", "Bearer " + customerToken))
        .andExpect(status().isForbidden());
  }
}
