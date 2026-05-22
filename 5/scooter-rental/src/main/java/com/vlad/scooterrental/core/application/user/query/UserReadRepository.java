package com.vlad.scooterrental.core.application.user.query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserReadRepository {
  Optional<UserView> findViewById(UUID userId);

  List<UserView> findAllViews();
}
