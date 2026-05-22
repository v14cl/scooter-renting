package com.vlad.scooterrental.infrastructure.config;

import com.vlad.scooterrental.application.auth.LoginCommandHandler;
import com.vlad.scooterrental.application.auth.RegisterUserCommandHandler;
import com.vlad.scooterrental.application.rental.command.CreateRentalCommandHandler;
import com.vlad.scooterrental.application.rental.command.DeleteRentalCommandHandler;
import com.vlad.scooterrental.application.rental.command.UpdateRentalCommandHandler;
import com.vlad.scooterrental.application.rental.query.GetRentalByIdQueryHandler;
import com.vlad.scooterrental.application.rental.query.ListRentalsQueryHandler;
import com.vlad.scooterrental.application.rental.query.RentalReadRepository;
import com.vlad.scooterrental.application.scooter.command.CreateScooterCommandHandler;
import com.vlad.scooterrental.application.scooter.command.DeleteScooterCommandHandler;
import com.vlad.scooterrental.application.scooter.command.UpdateScooterCommandHandler;
import com.vlad.scooterrental.application.scooter.query.GetScooterByIdQueryHandler;
import com.vlad.scooterrental.application.scooter.query.ListScootersQueryHandler;
import com.vlad.scooterrental.application.scooter.query.ScooterReadRepository;
import com.vlad.scooterrental.application.user.command.CreateUserCommandHandler;
import com.vlad.scooterrental.application.user.command.DeleteUserCommandHandler;
import com.vlad.scooterrental.application.user.command.UpdateUserCommandHandler;
import com.vlad.scooterrental.application.user.query.GetCurrentUserQueryHandler;
import com.vlad.scooterrental.application.user.query.GetUserByIdQueryHandler;
import com.vlad.scooterrental.application.user.query.ListUsersQueryHandler;
import com.vlad.scooterrental.application.user.query.UserReadRepository;
import com.vlad.scooterrental.domain.factory.RentalFactory;
import com.vlad.scooterrental.domain.factory.ScooterFactory;
import com.vlad.scooterrental.domain.factory.UserFactory;
import com.vlad.scooterrental.domain.repository.PasswordHasher;
import com.vlad.scooterrental.domain.repository.RentalRepository;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import com.vlad.scooterrental.domain.repository.TokenProvider;
import com.vlad.scooterrental.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public RegisterUserCommandHandler registerUserCommandHandler(
      UserFactory userFactory,
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      TokenProvider tokenProvider) {
    return new RegisterUserCommandHandler(
        userFactory, userRepository, passwordHasher, tokenProvider);
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
      ScooterFactory scooterFactory, ScooterRepository scooterRepository) {
    return new CreateScooterCommandHandler(scooterFactory, scooterRepository);
  }

  @Bean
  public UpdateScooterCommandHandler updateScooterCommandHandler(
      ScooterFactory scooterFactory, ScooterRepository scooterRepository) {
    return new UpdateScooterCommandHandler(scooterFactory, scooterRepository);
  }

  @Bean
  public DeleteScooterCommandHandler deleteScooterCommandHandler(
      ScooterRepository scooterRepository) {
    return new DeleteScooterCommandHandler(scooterRepository);
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
      ScooterRepository scooterRepository) {
    return new CreateRentalCommandHandler(rentalFactory, rentalRepository, scooterRepository);
  }

  @Bean
  public UpdateRentalCommandHandler updateRentalCommandHandler(
      RentalFactory rentalFactory,
      RentalRepository rentalRepository,
      ScooterRepository scooterRepository) {
    return new UpdateRentalCommandHandler(rentalFactory, rentalRepository, scooterRepository);
  }

  @Bean
  public DeleteRentalCommandHandler deleteRentalCommandHandler(RentalRepository rentalRepository) {
    return new DeleteRentalCommandHandler(rentalRepository);
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
