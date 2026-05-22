package com.vlad.scooterrental.application.user.query;

import com.vlad.scooterrental.domain.exception.NotFoundException;

public class GetUserByIdQueryHandler {
  private final UserReadRepository userReadRepository;

  public GetUserByIdQueryHandler(UserReadRepository userReadRepository) {
    this.userReadRepository = userReadRepository;
  }

  public UserView handle(GetUserByIdQuery query) {
    return userReadRepository
        .findViewById(query.userId())
        .orElseThrow(() -> new NotFoundException("User not found"));
  }
}
