package com.vlad.scooterrental.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vlad.scooterrental.application.audit.AuditAction;
import com.vlad.scooterrental.application.audit.AuditMessage;
import com.vlad.scooterrental.application.audit.AuditRecorder;
import com.vlad.scooterrental.application.audit.SafeAuditRecorder;
import com.vlad.scooterrental.application.auth.RegisterUserCommand;
import com.vlad.scooterrental.application.auth.RegisterUserCommandHandler;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.UserFactory;
import com.vlad.scooterrental.domain.model.User;
import com.vlad.scooterrental.domain.repository.PasswordHasher;
import com.vlad.scooterrental.domain.repository.UserRepository;
import com.vlad.scooterrental.domain.value.Email;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RegisterUserCommandHandlerAuditTest {

  @Test
  void shouldAuditAfterSuccessfulRegistration() {
    FakeUserRepository repository = new FakeUserRepository(false);
    RecordingAuditRecorder auditRecorder = new RecordingAuditRecorder();
    RegisterUserCommandHandler handler =
        new RegisterUserCommandHandler(
            new UserFactory(repository), repository, new PlainPasswordHasher(), auditRecorder);

    UUID userId =
        handler.handle(new RegisterUserCommand("Ivan Rider", "ivan@example.com", "password1"));

    assertEquals(repository.saved.id(), userId);
    assertEquals(AuditAction.USER_REGISTERED, auditRecorder.recorded.action());
    assertEquals(userId.toString(), auditRecorder.recorded.actorId());
    assertEquals(userId.toString(), auditRecorder.recorded.entityId());
  }

  @Test
  void shouldStillRegisterWhenSafeAuditRecorderSuppressesFailure() {
    FakeUserRepository repository = new FakeUserRepository(false);
    RegisterUserCommandHandler handler =
        new RegisterUserCommandHandler(
            new UserFactory(repository),
            repository,
            new PlainPasswordHasher(),
            new SafeAuditRecorder(
                message -> {
                  throw new IllegalStateException("audit unavailable");
                }));

    UUID userId =
        handler.handle(new RegisterUserCommand("Ivan Rider", "ivan@example.com", "password1"));

    assertEquals(repository.saved.id(), userId);
  }

  @Test
  void shouldNotAuditWhenRegistrationValidationFails() {
    FakeUserRepository repository = new FakeUserRepository(false);
    RecordingAuditRecorder auditRecorder = new RecordingAuditRecorder();
    RegisterUserCommandHandler handler =
        new RegisterUserCommandHandler(
            new UserFactory(repository), repository, new PlainPasswordHasher(), auditRecorder);

    assertThrows(
        ValidationException.class,
        () -> handler.handle(new RegisterUserCommand("Ivan Rider", "ivan@example.com", "")));

    assertEquals(0, auditRecorder.callCount);
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

  private static class PlainPasswordHasher implements PasswordHasher {
    @Override
    public String hash(String rawPassword) {
      return "hashed:" + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash) {
      return passwordHash.equals(hash(rawPassword));
    }
  }

  private static class FakeUserRepository implements UserRepository {
    private final boolean emailExists;
    private User saved;

    private FakeUserRepository(boolean emailExists) {
      this.emailExists = emailExists;
    }

    @Override
    public Optional<User> findById(UUID id) {
      return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(Email email) {
      return Optional.empty();
    }

    @Override
    public boolean existsByEmail(Email email) {
      return emailExists;
    }

    @Override
    public boolean existsByEmailAndIdNot(Email email, UUID id) {
      return false;
    }

    @Override
    public User save(User user) {
      saved = user;
      return user;
    }

    @Override
    public List<User> findAll() {
      return List.of();
    }

    @Override
    public void deleteById(UUID id) {}
  }
}
