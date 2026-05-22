package com.vlad.scooterrental.core.application.user.query;

import com.vlad.scooterrental.core.domain.model.User;
import java.util.UUID;

public record UserView(UUID id, String fullName, String email, String role) {
  public static UserView from(User user) {
    return new UserView(user.id(), user.fullName(), user.email().value(), user.role().name());
  }
}
