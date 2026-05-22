package com.vlad.scooterrental.core.domain.repository;

import com.vlad.scooterrental.core.domain.model.User;
import com.vlad.scooterrental.core.domain.value.Email;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  Optional<User> findById(UUID id);

  Optional<User> findByEmail(Email email);

  boolean existsByEmail(Email email);

  boolean existsByEmailAndIdNot(Email email, UUID id);

  User save(User user);

  List<User> findAll();

  void deleteById(UUID id);
}
