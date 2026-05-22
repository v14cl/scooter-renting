package com.vlad.scooterrental.infrastructure.config;

import com.vlad.scooterrental.domain.factory.UserFactory;
import com.vlad.scooterrental.domain.model.Role;
import com.vlad.scooterrental.domain.repository.PasswordHasher;
import com.vlad.scooterrental.domain.repository.UserRepository;
import com.vlad.scooterrental.domain.value.Email;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminUserInitializer {

  @Bean
  public ApplicationRunner createDefaultAdmin(
      UserRepository userRepository, PasswordHasher passwordHasher, UserFactory userFactory) {
    return args -> {
      if (!userRepository.existsByEmail(Email.of("admin@scooter.local"))) {
        var admin =
            userFactory.create(
                "System Admin",
                "admin@scooter.local",
                passwordHasher.hash("admin12345"),
                Role.ADMIN);
        userRepository.save(admin);
      }
    };
  }
}
