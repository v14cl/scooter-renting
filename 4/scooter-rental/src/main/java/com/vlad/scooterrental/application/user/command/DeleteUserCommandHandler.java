package com.vlad.scooterrental.application.user.command;

import com.vlad.scooterrental.domain.exception.ConflictException;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.repository.UserRepository;

public class DeleteUserCommandHandler {
  private final UserRepository userRepository;

  public DeleteUserCommandHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void handle(DeleteUserCommand command) {
    var user =
        userRepository
            .findById(command.userId())
            .orElseThrow(() -> new NotFoundException("User not found"));
    if ("admin@scooter.local".equals(user.email().value())) {
      throw new ConflictException("Default admin user cannot be deleted");
    }
    userRepository.deleteById(command.userId());
  }
}
