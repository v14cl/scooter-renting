package com.vlad.scooterrental.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vlad.scooterrental.application.audit.AuditMessage;
import com.vlad.scooterrental.application.audit.AuditRecorder;
import com.vlad.scooterrental.application.audit.SafeAuditRecorder;
import com.vlad.scooterrental.application.scooter.command.CreateScooterCommand;
import com.vlad.scooterrental.application.scooter.command.CreateScooterCommandHandler;
import com.vlad.scooterrental.domain.exception.ConflictException;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.ScooterFactory;
import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CreateScooterCommandHandlerTest {

  @Test
  void shouldCreateScooterAndReturnId() {
    FakeScooterRepository repository = new FakeScooterRepository(false);
    RecordingAuditRecorder auditRecorder = new RecordingAuditRecorder();
    CreateScooterCommandHandler handler =
        new CreateScooterCommandHandler(new ScooterFactory(repository), repository, auditRecorder);

    UUID scooterId =
        handler.handle(
            new CreateScooterCommand(
                " sct-300 ", "Ninebot Max", "available", 90, new BigDecimal("2.50")));

    assertEquals(repository.saved.id(), scooterId);
    assertEquals("SCT-300", repository.saved.code());
    assertTrue(repository.saveCalled);
    assertEquals(repository.saved.id().toString(), auditRecorder.recorded.entityId());
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
  void shouldStillCreateScooterWhenSafeAuditRecorderSuppressesFailure() {
    FakeScooterRepository repository = new FakeScooterRepository(false);
    CreateScooterCommandHandler handler =
        new CreateScooterCommandHandler(
            new ScooterFactory(repository),
            repository,
            new SafeAuditRecorder(
                message -> {
                  throw new IllegalStateException("audit unavailable");
                }));

    UUID scooterId =
        handler.handle(
            new CreateScooterCommand(
                "SCT-303", "Ninebot Max", "AVAILABLE", 90, new BigDecimal("2.50")));

    assertEquals(repository.saved.id(), scooterId);
    assertTrue(repository.saveCalled);
  }

  @Test
  void shouldNotAuditWhenDomainValidationFails() {
    FakeScooterRepository repository = new FakeScooterRepository(false);
    RecordingAuditRecorder auditRecorder = new RecordingAuditRecorder();
    CreateScooterCommandHandler handler =
        new CreateScooterCommandHandler(new ScooterFactory(repository), repository, auditRecorder);

    assertThrows(
        ValidationException.class,
        () ->
            handler.handle(
                new CreateScooterCommand(
                    "SCT-304", "Ninebot Max", "UNKNOWN", 90, new BigDecimal("2.50"))));

    assertEquals(0, auditRecorder.callCount);
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

  private static class RecordingAuditRecorder implements AuditRecorder {
    private AuditMessage recorded;
    private int callCount;

    @Override
    public void record(AuditMessage message) {
      recorded = message;
      callCount++;
    }
  }
}
