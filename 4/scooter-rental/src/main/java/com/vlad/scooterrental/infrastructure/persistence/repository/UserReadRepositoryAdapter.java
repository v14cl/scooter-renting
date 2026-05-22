package com.vlad.scooterrental.infrastructure.persistence.repository;

import com.vlad.scooterrental.application.user.query.UserReadRepository;
import com.vlad.scooterrental.application.user.query.UserView;
import com.vlad.scooterrental.infrastructure.persistence.jpa.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class UserReadRepositoryAdapter implements UserReadRepository {

  private final UserJpaRepository userJpaRepository;

  public UserReadRepositoryAdapter(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public Optional<UserView> findViewById(UUID userId) {
    return userJpaRepository
        .findById(userId)
        .map(
            entity ->
                new UserView(
                    entity.getId(), entity.getFullName(), entity.getEmail(), entity.getRole()));
  }

  @Override
  public List<UserView> findAllViews() {
    return userJpaRepository.findAll(Sort.by("fullName").ascending()).stream()
        .map(
            entity ->
                new UserView(
                    entity.getId(), entity.getFullName(), entity.getEmail(), entity.getRole()))
        .toList();
  }
}
