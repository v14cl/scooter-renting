package com.vlad.scooterrental.application.user.command;

import com.vlad.scooterrental.application.auth.UserView;
import com.vlad.scooterrental.domain.exception.ValidationException;
import com.vlad.scooterrental.domain.factory.UserFactory;
import com.vlad.scooterrental.domain.repository.PasswordHasher;
import com.vlad.scooterrental.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateUserCommandHandler {

    private final UserFactory userFactory;
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public CreateUserCommandHandler(
            UserFactory userFactory,
            UserRepository userRepository,
            PasswordHasher passwordHasher
    ) {
        this.userFactory = userFactory;
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public UserView handle(CreateUserCommand command) {
        if (command.password() == null || command.password().isBlank()) {
            throw new ValidationException("Password must not be blank");
        }

        var user = userFactory.create(
                command.fullName(),
                command.email(),
                passwordHasher.hash(command.password()),
                command.role(),
                userRepository
        );
        return UserView.from(userRepository.save(user));
    }
}
