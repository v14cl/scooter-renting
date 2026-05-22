package com.vlad.scooterrental.infrastructure.config;

import com.vlad.scooterrental.domain.factory.RentalFactory;
import com.vlad.scooterrental.domain.factory.ScooterFactory;
import com.vlad.scooterrental.domain.factory.UserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class DomainConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public UserFactory userFactory() {
        return new UserFactory();
    }

    @Bean
    public ScooterFactory scooterFactory() {
        return new ScooterFactory();
    }

    @Bean
    public RentalFactory rentalFactory(Clock clock) {
        return new RentalFactory(clock);
    }
}
