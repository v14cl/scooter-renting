package com.vlad.scooterrental.application.user.query;

import com.vlad.scooterrental.application.auth.UserView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserReadRepository {

    Optional<UserView> findViewById(UUID userId);

    List<UserView> findAllViews();
}
