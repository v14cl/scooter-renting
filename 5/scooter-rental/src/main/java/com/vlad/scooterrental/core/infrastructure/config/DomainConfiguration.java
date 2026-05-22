package com.vlad.scooterrental.core.infrastructure.config;

import com.vlad.scooterrental.core.domain.factory.RentalFactory;
import com.vlad.scooterrental.core.domain.factory.ScooterFactory;
import com.vlad.scooterrental.core.domain.factory.UserFactory;
import com.vlad.scooterrental.core.domain.repository.RentalRepository;
import com.vlad.scooterrental.core.domain.repository.ScooterRepository;
import com.vlad.scooterrental.core.domain.repository.UserRepository;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  public UserFactory userFactory(UserRepository userRepository) {
    return new UserFactory(userRepository);
  }

  @Bean
  public ScooterFactory scooterFactory(ScooterRepository scooterRepository) {
    return new ScooterFactory(scooterRepository);
  }

  @Bean
  public RentalFactory rentalFactory(
      Clock clock, RentalRepository rentalRepository, ScooterRepository scooterRepository) {
    return new RentalFactory(clock, rentalRepository, scooterRepository);
  }
}
