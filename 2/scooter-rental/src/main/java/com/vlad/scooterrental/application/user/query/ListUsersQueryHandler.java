package com.vlad.scooterrental.application.user.query;

import com.vlad.scooterrental.application.auth.UserView;
import java.util.List;

public class ListUsersQueryHandler {
  private final UserReadRepository userReadRepository;

  public ListUsersQueryHandler(UserReadRepository userReadRepository) {
    this.userReadRepository = userReadRepository;
  }

  public List<UserView> handle() {
    return userReadRepository.findAllViews();
  }
}
