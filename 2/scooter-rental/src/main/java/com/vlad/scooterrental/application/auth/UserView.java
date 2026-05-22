package com.vlad.scooterrental.application.auth;

import com.vlad.scooterrental.domain.model.User;
import java.util.UUID;

public record UserView(UUID id, String fullName, String email, String role) {
  public static UserView from(User user) {
    return new UserView(user.id(), user.fullName(), user.email().value(), user.role().name());
  }
}
