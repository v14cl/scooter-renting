package com.vlad.scooterrental.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vlad.scooterrental.application.common.Actor;
import com.vlad.scooterrental.application.event.EventPublisher;
import com.vlad.scooterrental.application.event.IntegrationEvent;
import com.vlad.scooterrental.application.event.events.RentalCreatedEvent;
import com.vlad.scooterrental.application.event.events.RentalDeletedEvent;
import com.vlad.scooterrental.application.rental.command.CreateRentalCommand;
import com.vlad.scooterrental.application.rental.command.CreateRentalCommandHandler;
import com.vlad.scooterrental.application.rental.command.DeleteRentalCommand;
import com.vlad.scooterrental.application.rental.command.DeleteRentalCommandHandler;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.RentalFactory;
import com.vlad.scooterrental.domain.model.Rental;
import com.vlad.scooterrental.domain.model.Role;
import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.domain.repository.RentalRepository;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import com.vlad.scooterrental.domain.value.PricePerMinute;
import com.vlad.scooterrental.domain.value.RentalPeriod;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RentalEventPublishingTest {

  @Test
  void shouldPublishRentalCreatedEventAfterSuccessfulCreation() {
    Clock clock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);
    UUID scooterId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    InMemoryRentalRepository rentalRepository = new InMemoryRentalRepository();
    InMemoryScooterRepository scooterRepository = new InMemoryScooterRepository(scooterId);
    RecordingEventPublisher eventPublisher = new RecordingEventPublisher();
    CreateRentalCommandHandler handler =
        new CreateRentalCommandHandler(
            new RentalFactory(clock, rentalRepository, scooterRepository),
            rentalRepository,
            eventPublisher);

    UUID rentalId =
        handler.handle(
            new CreateRentalCommand(
                new Actor(actorId, Role.CUSTOMER),
                scooterId,
                LocalDateTime.of(2026, 1, 1, 11, 0),
                LocalDateTime.of(2026, 1, 1, 12, 0)));

    RentalCreatedEvent event = assertInstanceOf(RentalCreatedEvent.class, eventPublisher.last());
    assertEquals(rentalId, event.rentalId());
    assertEquals(scooterId, event.scooterId());
    assertEquals(actorId, event.renterId());
    assertEquals(actorId, event.actorId());
    assertEquals(LocalDateTime.of(2026, 1, 1, 11, 0), event.startTime());
    assertEquals(LocalDateTime.of(2026, 1, 1, 12, 0), event.endTime());
  }

  @Test
  void shouldNotPublishEventWhenCreationFailsBeforeSave() {
    Clock clock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);
    UUID scooterId = UUID.randomUUID();
    InMemoryRentalRepository rentalRepository = new InMemoryRentalRepository();
    InMemoryScooterRepository scooterRepository = new InMemoryScooterRepository(scooterId);
    RecordingEventPublisher eventPublisher = new RecordingEventPublisher();
    CreateRentalCommandHandler handler =
        new CreateRentalCommandHandler(
            new RentalFactory(clock, rentalRepository, scooterRepository),
            rentalRepository,
            eventPublisher);

    assertThrows(
        ValidationException.class,
        () ->
            handler.handle(
                new CreateRentalCommand(
                    new Actor(UUID.randomUUID(), Role.CUSTOMER),
                    scooterId,
                    LocalDateTime.of(2026, 1, 1, 12, 0),
                    LocalDateTime.of(2026, 1, 1, 11, 0))));

    assertEquals(0, eventPublisher.events.size());
  }

  @Test
  void shouldPublishRentalDeletedEventAfterSuccessfulDeletion() {
    UUID ownerId = UUID.randomUUID();
    UUID scooterId = UUID.randomUUID();
    Rental rental =
        new Rental(
            UUID.randomUUID(),
            ownerId,
            scooterId,
            RentalPeriod.restore(
                LocalDateTime.of(2026, 1, 1, 11, 0),
                LocalDateTime.of(2026, 1, 1, 12, 0)));
    InMemoryRentalRepository rentalRepository = new InMemoryRentalRepository(rental);
    RecordingEventPublisher eventPublisher = new RecordingEventPublisher();
    DeleteRentalCommandHandler handler =
        new DeleteRentalCommandHandler(rentalRepository, eventPublisher);

    handler.handle(new DeleteRentalCommand(new Actor(ownerId, Role.CUSTOMER), rental.id()));

    RentalDeletedEvent event = assertInstanceOf(RentalDeletedEvent.class, eventPublisher.last());
    assertEquals(rental.id(), event.rentalId());
    assertEquals(ownerId, event.renterId());
    assertEquals(ownerId, event.actorId());
    assertEquals(rental.id(), rentalRepository.deletedId);
  }

  private static class RecordingEventPublisher implements EventPublisher {
    private final List<IntegrationEvent> events = new ArrayList<>();

    @Override
    public void publish(IntegrationEvent event) {
      events.add(event);
    }

    private IntegrationEvent last() {
      return events.get(events.size() - 1);
    }
  }

  private static class InMemoryRentalRepository implements RentalRepository {
    private final List<Rental> rentals = new ArrayList<>();
    private UUID deletedId;

    private InMemoryRentalRepository(Rental... rentals) {
      this.rentals.addAll(List.of(rentals));
    }

    @Override
    public Optional<Rental> findById(UUID id) {
      return rentals.stream().filter(rental -> rental.id().equals(id)).findFirst();
    }

    @Override
    public List<Rental> findAll() {
      return List.copyOf(rentals);
    }

    @Override
    public List<Rental> findByRenterId(UUID renterId) {
      return rentals.stream().filter(rental -> rental.renterId().equals(renterId)).toList();
    }

    @Override
    public boolean hasOverlap(UUID scooterId, RentalPeriod rentalPeriod, UUID ignoredRentalId) {
      return false;
    }

    @Override
    public Rental save(Rental rental) {
      rentals.removeIf(existing -> existing.id().equals(rental.id()));
      rentals.add(rental);
      return rental;
    }

    @Override
    public void deleteById(UUID id) {
      deletedId = id;
      rentals.removeIf(rental -> rental.id().equals(id));
    }
  }

  private static class InMemoryScooterRepository implements ScooterRepository {
    private final Scooter scooter;

    private InMemoryScooterRepository(UUID scooterId) {
      this.scooter =
          new Scooter(
              scooterId,
              "SCT-900",
              "Ninebot",
              ScooterStatus.AVAILABLE,
              80,
              PricePerMinute.of(new BigDecimal("2.10")));
    }

    @Override
    public Optional<Scooter> findById(UUID id) {
      return Optional.of(scooter);
    }

    @Override
    public List<Scooter> findAll() {
      return List.of(scooter);
    }

    @Override
    public boolean existsByCode(String code) {
      return false;
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, UUID id) {
      return false;
    }

    @Override
    public Scooter save(Scooter scooter) {
      return scooter;
    }

    @Override
    public void deleteById(UUID id) {}
  }
}
