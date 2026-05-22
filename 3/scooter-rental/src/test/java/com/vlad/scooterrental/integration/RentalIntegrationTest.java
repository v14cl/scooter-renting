package com.vlad.scooterrental.integration;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RentalIntegrationTest extends IntegrationTestSupport {

    @Test
    void shouldCreateRentalAndHideOtherUsersRentals() throws Exception {
        String adminToken = loginAdmin();
        Map<String, Object> scooter = createScooter(adminToken, "SCT-200");

        String firstCustomerToken = registerCustomer("first@example.com");
        String secondCustomerToken = registerCustomer("second@example.com");

        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusHours(2);

        mockMvc.perform(post("/api/rentals")
                        .header("Authorization", "Bearer " + firstCustomerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(rentalPayload((String) scooter.get("id"), startTime, endTime))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.scooterId").value((String) scooter.get("id")));

        mockMvc.perform(get("/api/rentals")
                        .header("Authorization", "Bearer " + secondCustomerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/api/rentals")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].scooterId").value((String) scooter.get("id")));
    }

    @Test
    void shouldReturnConflictForOverlappingRental() throws Exception {
        String adminToken = loginAdmin();
        Map<String, Object> scooter = createScooter(adminToken, "SCT-201");

        String customerToken = registerCustomer("rider@example.com");
        LocalDateTime startTime = LocalDateTime.now().plusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusHours(2);

        mockMvc.perform(post("/api/rentals")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(rentalPayload((String) scooter.get("id"), startTime, endTime))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/rentals")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(rentalPayload((String) scooter.get("id"), startTime.plusMinutes(30), endTime.plusMinutes(30)))))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateRental() throws Exception {
        String adminToken = loginAdmin();
        Map<String, Object> scooter = createScooter(adminToken, "SCT-202");
        String customerToken = registerCustomer("update@example.com");

        LocalDateTime startTime = LocalDateTime.now().plusDays(3).withHour(8).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusHours(1);
        String createdRental = mockMvc.perform(post("/api/rentals")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(rentalPayload((String) scooter.get("id"), startTime, endTime))))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String rentalId = (String) read(createdRental).get("id");

        mockMvc.perform(put("/api/rentals/{rentalId}", rentalId)
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(json(rentalPayload(
                                (String) scooter.get("id"),
                                startTime.plusHours(1),
                                endTime.plusHours(1)
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value(
                        startTime.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                ));
    }
}
