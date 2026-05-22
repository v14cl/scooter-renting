package com.vlad.scooterrental.application.user.query;

import java.util.List;

public class ListUsersQueryHandler {
  private final UserReadRepository userReadRepository;

  public ListUsersQueryHandler(UserReadRepository userReadRepository) {
    this.userReadRepository = userReadRepository;
  }

  public List<UserView> handle(ListUsersQuery query) {
    return userReadRepository.findAllViews();
  }
}
