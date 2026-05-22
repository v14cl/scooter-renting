package com.vlad.scooterrental.core.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vlad.scooterrental.core.api.event.UserRegisteredEvent;
import com.vlad.scooterrental.core.application.auth.RegisterUserCommand;
import com.vlad.scooterrental.core.application.auth.RegisterUserCommandHandler;
import com.vlad.scooterrental.core.domain.exception.ValidationException;
import com.vlad.scooterrental.core.domain.factory.UserFactory;
import com.vlad.scooterrental.core.domain.model.User;
import com.vlad.scooterrental.core.domain.repository.PasswordHasher;
import com.vlad.scooterrental.core.domain.repository.UserRepository;
import com.vlad.scooterrental.core.domain.value.Email;
import com.vlad.scooterrental.shared.event.EventPublisher;
import com.vlad.scooterrental.shared.event.IntegrationEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RegisterUserCommandHandlerAuditTest {

  @Test
  void shouldPublishEventAfterSuccessfulRegistration() {
    FakeUserRepository repository = new FakeUserRepository(false);
    RecordingEventPublisher eventPublisher = new RecordingEventPublisher();
    RegisterUserCommandHandler handler =
        new RegisterUserCommandHandler(
            new UserFactory(repository), repository, new PlainPasswordHasher(), eventPublisher);

    UUID userId =
        handler.handle(new RegisterUserCommand("Ivan Rider", "ivan@example.com", "password1"));

    assertEquals(repository.saved.id(), userId);
    UserRegisteredEvent event = assertInstanceOf(UserRegisteredEvent.class, eventPublisher.last());
    assertEquals(userId, event.userId());
    assertEquals("ivan@example.com", event.email());
    assertEquals("Ivan Rider", event.fullName());
  }

  @Test
  void shouldStillRegisterWhenEventSubscriberFailsOutsideCommandHandler() {
    FakeUserRepository repository = new FakeUserRepository(false);
    RegisterUserCommandHandler handler =
        new RegisterUserCommandHandler(
            new UserFactory(repository),
            repository,
            new PlainPasswordHasher(),
            event -> {});

    UUID userId =
        handler.handle(new RegisterUserCommand("Ivan Rider", "ivan@example.com", "password1"));

    assertEquals(repository.saved.id(), userId);
  }

  @Test
  void shouldNotPublishEventWhenRegistrationValidationFails() {
    FakeUserRepository repository = new FakeUserRepository(false);
    RecordingEventPublisher eventPublisher = new RecordingEventPublisher();
    RegisterUserCommandHandler handler =
        new RegisterUserCommandHandler(
            new UserFactory(repository), repository, new PlainPasswordHasher(), eventPublisher);

    assertThrows(
        ValidationException.class,
        () -> handler.handle(new RegisterUserCommand("Ivan Rider", "ivan@example.com", "")));

    assertEquals(0, eventPublisher.events.size());
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
