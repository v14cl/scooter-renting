package com.vlad.scooterrental.analytics.infrastructure.persistence;

import com.vlad.scooterrental.analytics.api.AnalyticsDashboardView;
import com.vlad.scooterrental.analytics.application.projection.AnalyticsProjectionRepository;
import com.vlad.scooterrental.analytics.domain.model.RentalActivity;
import com.vlad.scooterrental.analytics.domain.model.RentalActivityType;
import com.vlad.scooterrental.analytics.domain.model.ScooterActivity;
import com.vlad.scooterrental.analytics.domain.model.ScooterActivityType;
import com.vlad.scooterrental.analytics.domain.model.UserRegistrationActivity;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAnalyticsProjectionRepository implements AnalyticsProjectionRepository {
  private final Clock clock;
  private final Set<UUID> processedEventIds = new HashSet<>();
  private final Map<UUID, String> scooterStatuses = new HashMap<>();
  private final Map<UUID, RentalState> rentalStates = new HashMap<>();
  private final Map<LocalDate, Long> rentalsByDay = new HashMap<>();
  private long totalUsersRegistered;
  private long totalScootersCreated;
  private long totalRentalsCreated;
  private long deletedRentalsCount;
  private Instant lastActivityAt;

  public InMemoryAnalyticsProjectionRepository(Clock clock) {
    this.clock = clock;
  }

  public synchronized void clear() {
    processedEventIds.clear();
    scooterStatuses.clear();
    rentalStates.clear();
    rentalsByDay.clear();
    totalUsersRegistered = 0;
    totalScootersCreated = 0;
    totalRentalsCreated = 0;
    deletedRentalsCount = 0;
    lastActivityAt = null;
  }

  @Override
  public synchronized void apply(UserRegistrationActivity activity) {
    if (!markProcessed(activity.eventId())) {
      return;
    }
    totalUsersRegistered++;
    touch(activity.occurredAt());
  }

  @Override
  public synchronized void apply(ScooterActivity activity) {
    if (!markProcessed(activity.eventId())) {
      return;
    }
    if (activity.activityType() == ScooterActivityType.CREATED) {
      totalScootersCreated++;
    }
    scooterStatuses.put(activity.scooterId(), activity.status());
    touch(activity.occurredAt());
  }

  @Override
  public synchronized void apply(RentalActivity activity) {
    if (!markProcessed(activity.eventId())) {
      return;
    }
    if (activity.activityType() == RentalActivityType.CREATED) {
      totalRentalsCreated++;
      rentalsByDay.merge(activity.startTime().toLocalDate(), 1L, Long::sum);
    }
    if (activity.activityType() == RentalActivityType.DELETED) {
      deletedRentalsCount++;
      rentalStates.put(activity.rentalId(), RentalState.DELETED);
    } else {
      rentalStates.put(activity.rentalId(), rentalState(activity.endTime()));
    }
    touch(activity.occurredAt());
  }

  @Override
  public synchronized AnalyticsDashboardView dashboard() {
    long activeRentals = rentalStates.values().stream().filter(RentalState.ACTIVE::equals).count();
    long completedRentals =
        rentalStates.values().stream().filter(RentalState.COMPLETED::equals).count();
    Map<String, Long> scootersByStatus = new HashMap<>();
    scooterStatuses.values().forEach(status -> scootersByStatus.merge(status, 1L, Long::sum));
    return new AnalyticsDashboardView(
        totalUsersRegistered,
        totalScootersCreated,
        totalRentalsCreated,
        activeRentals,
        completedRentals,
        deletedRentalsCount,
        Map.copyOf(rentalsByDay),
        Map.copyOf(scootersByStatus),
        lastActivityAt);
  }

  private boolean markProcessed(UUID eventId) {
    return processedEventIds.add(eventId);
  }

  private RentalState rentalState(LocalDateTime endTime) {
    if (endTime.isBefore(LocalDateTime.now(clock)) || endTime.isEqual(LocalDateTime.now(clock))) {
      return RentalState.COMPLETED;
    }
    return RentalState.ACTIVE;
  }

  private void touch(Instant occurredAt) {
    if (lastActivityAt == null || occurredAt.isAfter(lastActivityAt)) {
      lastActivityAt = occurredAt;
    }
  }

  private enum RentalState {
    ACTIVE,
    COMPLETED,
    DELETED
  }
}
