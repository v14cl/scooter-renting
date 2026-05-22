package com.vlad.scooterrental.application.user.command;

import com.vlad.scooterrental.application.auth.UserView;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.UserFactory;
import com.vlad.scooterrental.domain.repository.PasswordHasher;
import com.vlad.scooterrental.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserCommandHandler {

    private final UserFactory userFactory;
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UpdateUserCommandHandler(
            UserFactory userFactory,
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        this.userFactory = userFactory;
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public UserView handle(UpdateUserCommand command) {
        var existingUser = userRepository.findById(command.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        String passwordHash = existingUser.passwordHash();
        if (command.password() != null && !command.password().isBlank()) {
            passwordHash = passwordHasher.hash(command.password());
        }
        else if (passwordHash == null || passwordHash.isBlank()) {
            throw new ValidationException("Password must not be blank");
        }

        var updatedUser = userFactory.update(
                command.userId(),
                command.fullName(),
                command.email(),
                passwordHash,
                command.role(),
                userRepository
        );
        return UserView.from(userRepository.save(updatedUser));
    }
}
