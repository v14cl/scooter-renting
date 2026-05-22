package com.vlad.scooterrental.analytics.api;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

public record AnalyticsDashboardView(
    long totalUsersRegistered,
    long totalScootersCreated,
    long totalRentalsCreated,
    long activeRentalsCount,
    long completedRentalsCount,
    long deletedRentalsCount,
    Map<LocalDate, Long> rentalsByDay,
    Map<String, Long> scootersByStatus,
    Instant lastActivityAt) {}
