package com.vlad.scooterrental.core.infrastructure.config;

import com.vlad.scooterrental.core.application.auth.LoginCommandHandler;
import com.vlad.scooterrental.core.application.auth.RegisterUserCommandHandler;
import com.vlad.scooterrental.core.application.rental.command.CreateRentalCommandHandler;
import com.vlad.scooterrental.core.application.rental.command.DeleteRentalCommandHandler;
import com.vlad.scooterrental.core.application.rental.command.UpdateRentalCommandHandler;
import com.vlad.scooterrental.core.application.rental.query.GetRentalByIdQueryHandler;
import com.vlad.scooterrental.core.application.rental.query.ListRentalsQueryHandler;
import com.vlad.scooterrental.core.application.rental.query.RentalReadRepository;
import com.vlad.scooterrental.core.application.scooter.command.CreateScooterCommandHandler;
import com.vlad.scooterrental.core.application.scooter.command.DeleteScooterCommandHandler;
import com.vlad.scooterrental.core.application.scooter.command.UpdateScooterCommandHandler;
import com.vlad.scooterrental.core.application.scooter.query.GetScooterByIdQueryHandler;
import com.vlad.scooterrental.core.application.scooter.query.ListScootersQueryHandler;
import com.vlad.scooterrental.core.application.scooter.query.ScooterReadRepository;
import com.vlad.scooterrental.core.application.user.command.CreateUserCommandHandler;
import com.vlad.scooterrental.core.application.user.command.DeleteUserCommandHandler;
import com.vlad.scooterrental.core.application.user.command.UpdateUserCommandHandler;
import com.vlad.scooterrental.core.application.user.query.GetCurrentUserQueryHandler;
import com.vlad.scooterrental.core.application.user.query.GetUserByIdQueryHandler;
import com.vlad.scooterrental.core.application.user.query.ListUsersQueryHandler;
import com.vlad.scooterrental.core.application.user.query.UserReadRepository;
import com.vlad.scooterrental.core.domain.factory.RentalFactory;
import com.vlad.scooterrental.core.domain.factory.ScooterFactory;
import com.vlad.scooterrental.core.domain.factory.UserFactory;
import com.vlad.scooterrental.core.domain.repository.PasswordHasher;
import com.vlad.scooterrental.core.domain.repository.RentalRepository;
import com.vlad.scooterrental.core.domain.repository.ScooterRepository;
import com.vlad.scooterrental.core.domain.repository.TokenProvider;
import com.vlad.scooterrental.core.domain.repository.UserRepository;
import com.vlad.scooterrental.shared.event.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public RegisterUserCommandHandler registerUserCommandHandler(
      UserFactory userFactory,
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      EventPublisher eventPublisher) {
    return new RegisterUserCommandHandler(
        userFactory, userRepository, passwordHasher, eventPublisher);
  }

  @Bean
  public LoginCommandHandler loginCommandHandler(
      UserRepository userRepository, PasswordHasher passwordHasher, TokenProvider tokenProvider) {
    return new LoginCommandHandler(userRepository, passwordHasher, tokenProvider);
  }

  @Bean
  public CreateUserCommandHandler createUserCommandHandler(
      UserFactory userFactory, UserRepository userRepository, PasswordHasher passwordHasher) {
    return new CreateUserCommandHandler(userFactory, userRepository, passwordHasher);
  }

  @Bean
  public UpdateUserCommandHandler updateUserCommandHandler(
      UserFactory userFactory, UserRepository userRepository, PasswordHasher passwordHasher) {
    return new UpdateUserCommandHandler(userFactory, userRepository, passwordHasher);
  }

  @Bean
  public DeleteUserCommandHandler deleteUserCommandHandler(UserRepository userRepository) {
    return new DeleteUserCommandHandler(userRepository);
  }

  @Bean
  public GetCurrentUserQueryHandler getCurrentUserQueryHandler(
      UserReadRepository userReadRepository) {
    return new GetCurrentUserQueryHandler(userReadRepository);
  }

  @Bean
  public GetUserByIdQueryHandler getUserByIdQueryHandler(UserReadRepository userReadRepository) {
    return new GetUserByIdQueryHandler(userReadRepository);
  }

  @Bean
  public ListUsersQueryHandler listUsersQueryHandler(UserReadRepository userReadRepository) {
    return new ListUsersQueryHandler(userReadRepository);
  }

  @Bean
  public CreateScooterCommandHandler createScooterCommandHandler(
      ScooterFactory scooterFactory,
      ScooterRepository scooterRepository,
      EventPublisher eventPublisher) {
    return new CreateScooterCommandHandler(scooterFactory, scooterRepository, eventPublisher);
  }

  @Bean
  public UpdateScooterCommandHandler updateScooterCommandHandler(
      ScooterFactory scooterFactory,
      ScooterRepository scooterRepository,
      EventPublisher eventPublisher) {
    return new UpdateScooterCommandHandler(scooterFactory, scooterRepository, eventPublisher);
  }

  @Bean
  public DeleteScooterCommandHandler deleteScooterCommandHandler(
      ScooterRepository scooterRepository, EventPublisher eventPublisher) {
    return new DeleteScooterCommandHandler(scooterRepository, eventPublisher);
  }

  @Bean
  public GetScooterByIdQueryHandler getScooterByIdQueryHandler(
      ScooterReadRepository scooterReadRepository) {
    return new GetScooterByIdQueryHandler(scooterReadRepository);
  }

  @Bean
  public ListScootersQueryHandler listScootersQueryHandler(
      ScooterReadRepository scooterReadRepository) {
    return new ListScootersQueryHandler(scooterReadRepository);
  }

  @Bean
  public CreateRentalCommandHandler createRentalCommandHandler(
      RentalFactory rentalFactory,
      RentalRepository rentalRepository,
      EventPublisher eventPublisher) {
    return new CreateRentalCommandHandler(rentalFactory, rentalRepository, eventPublisher);
  }

  @Bean
  public UpdateRentalCommandHandler updateRentalCommandHandler(
      RentalFactory rentalFactory,
      RentalRepository rentalRepository,
      EventPublisher eventPublisher) {
    return new UpdateRentalCommandHandler(rentalFactory, rentalRepository, eventPublisher);
  }

  @Bean
  public DeleteRentalCommandHandler deleteRentalCommandHandler(
      RentalRepository rentalRepository, EventPublisher eventPublisher) {
    return new DeleteRentalCommandHandler(rentalRepository, eventPublisher);
  }

  @Bean
  public GetRentalByIdQueryHandler getRentalByIdQueryHandler(
      RentalReadRepository rentalReadRepository) {
    return new GetRentalByIdQueryHandler(rentalReadRepository);
  }

  @Bean
  public ListRentalsQueryHandler listRentalsQueryHandler(
      RentalReadRepository rentalReadRepository) {
    return new ListRentalsQueryHandler(rentalReadRepository);
  }
}
