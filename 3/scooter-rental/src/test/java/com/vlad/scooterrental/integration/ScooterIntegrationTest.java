package com.vlad.scooterrental.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ScooterIntegrationTest extends IntegrationTestSupport {

    @Test
    void adminShouldManageScooterCrud() throws Exception {
        String adminToken = loginAdmin();
        String scooterId = (String) createScooter(adminToken, "SCT-100").get("id");

        mockMvc.perform(get("/api/scooters")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("SCT-100"));

        mockMvc.perform(put("/api/scooters/{scooterId}", scooterId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "code", "SCT-100",
                                "model", "Ninebot Updated",
                                "status", "MAINTENANCE",
                                "batteryLevel", 70,
                                "pricePerMinute", 3.10
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("MAINTENANCE"));

        mockMvc.perform(delete("/api/scooters/{scooterId}", scooterId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void customerShouldNotCreateScooter() throws Exception {
        String customerToken = registerCustomer("customer@example.com");

        mockMvc.perform(post("/api/scooters")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "code", "SCT-403",
                                "model", "Forbidden",
                                "status", "AVAILABLE",
                                "batteryLevel", 90,
                                "pricePerMinute", 2.20
                        ))))
                .andExpect(status().isForbidden());
    }
}
