package com.vlad.scooterrental.core.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vlad.scooterrental.core.api.event.ScooterCreatedEvent;
import com.vlad.scooterrental.core.application.scooter.command.CreateScooterCommand;
import com.vlad.scooterrental.core.application.scooter.command.CreateScooterCommandHandler;
import com.vlad.scooterrental.core.domain.exception.ConflictException;
import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.factory.ScooterFactory;
import com.vlad.scooterrental.core.domain.model.Scooter;
import com.vlad.scooterrental.core.domain.repository.ScooterRepository;
import com.vlad.scooterrental.shared.event.EventPublisher;
import com.vlad.scooterrental.shared.event.IntegrationEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CreateScooterCommandHandlerTest {

  @Test
  void shouldCreateScooterAndReturnId() {
    FakeScooterRepository repository = new FakeScooterRepository(false);
    RecordingEventPublisher eventPublisher = new RecordingEventPublisher();
    CreateScooterCommandHandler handler =
        new CreateScooterCommandHandler(new ScooterFactory(repository), repository, eventPublisher);

    UUID scooterId =
        handler.handle(
            new CreateScooterCommand(
                " sct-300 ", "Ninebot Max", "available", 90, new BigDecimal("2.50")));

    assertEquals(repository.saved.id(), scooterId);
    assertEquals("SCT-300", repository.saved.code());
    assertTrue(repository.saveCalled);
    ScooterCreatedEvent event = assertInstanceOf(ScooterCreatedEvent.class, eventPublisher.last());
    assertEquals(repository.saved.id(), event.scooterId());
    assertEquals("SCT-300", event.code());
    assertEquals("AVAILABLE", event.status());
  }

  @Test
  void shouldRejectNonPositivePriceThroughDomainFactory() {
    FakeScooterRepository repository = new FakeScooterRepository(false);
    CreateScooterCommandHandler handler =
        new CreateScooterCommandHandler(
            new ScooterFactory(repository), repository, message -> {});

    assertThrows(
        ValidationException.class,
        () ->
            handler.handle(
                new CreateScooterCommand(
                    "SCT-301", "Ninebot Max", "AVAILABLE", 90, BigDecimal.ZERO)));
  }

  @Test
  void shouldRejectDuplicateScooterCode() {
    FakeScooterRepository repository = new FakeScooterRepository(true);
    CreateScooterCommandHandler handler =
        new CreateScooterCommandHandler(
            new ScooterFactory(repository), repository, message -> {});

    assertThrows(
        ConflictException.class,
        () ->
            handler.handle(
                new CreateScooterCommand(
                    "SCT-302", "Ninebot Max", "AVAILABLE", 90, new BigDecimal("2.50"))));
  }

  @Test
  void shouldStillCreateScooterWhenEventPublisherDoesNotBlock() {
    FakeScooterRepository repository = new FakeScooterRepository(false);
    CreateScooterCommandHandler handler =
        new CreateScooterCommandHandler(
            new ScooterFactory(repository),
            repository,
            event -> {});

    UUID scooterId =
        handler.handle(
            new CreateScooterCommand(
                "SCT-303", "Ninebot Max", "AVAILABLE", 90, new BigDecimal("2.50")));

    assertEquals(repository.saved.id(), scooterId);
    assertTrue(repository.saveCalled);
  }

  @Test
  void shouldNotPublishEventWhenDomainValidationFails() {
    FakeScooterRepository repository = new FakeScooterRepository(false);
    RecordingEventPublisher eventPublisher = new RecordingEventPublisher();
    CreateScooterCommandHandler handler =
        new CreateScooterCommandHandler(new ScooterFactory(repository), repository, eventPublisher);

    assertThrows(
        ValidationException.class,
        () ->
            handler.handle(
                new CreateScooterCommand(
                    "SCT-304", "Ninebot Max", "UNKNOWN", 90, new BigDecimal("2.50"))));

    assertEquals(0, eventPublisher.events.size());
  }

  private static class FakeScooterRepository implements ScooterRepository {
    private final boolean codeExists;
    private Scooter saved;
    private boolean saveCalled;

    private FakeScooterRepository(boolean codeExists) {
      this.codeExists = codeExists;
    }

    @Override
    public Optional<Scooter> findById(UUID id) {
      return Optional.empty();
    }

    @Override
    public List<Scooter> findAll() {
      return List.of();
    }

    @Override
    public boolean existsByCode(String code) {
      return codeExists;
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, UUID id) {
      return false;
    }

    @Override
    public Scooter save(Scooter scooter) {
      saved = scooter;
      saveCalled = true;
      return scooter;
    }

    @Override
    public void deleteById(UUID id) {}
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
}
