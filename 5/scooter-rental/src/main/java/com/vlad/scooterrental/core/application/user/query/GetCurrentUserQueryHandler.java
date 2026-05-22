package com.vlad.scooterrental.core.application.user.query;

import com.vlad.scooterrental.core.domain.exception.NotFoundException;

public class GetCurrentUserQueryHandler {
  private final UserReadRepository userReadRepository;

  public GetCurrentUserQueryHandler(UserReadRepository userReadRepository) {
    this.userReadRepository = userReadRepository;
  }

  public UserView handle(GetCurrentUserQuery query) {
    return userReadRepository
        .findViewById(query.userId())
        .orElseThrow(() -> new NotFoundException("User not found"));
  }
}
