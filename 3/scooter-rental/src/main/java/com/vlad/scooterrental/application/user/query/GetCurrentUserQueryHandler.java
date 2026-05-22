package com.vlad.scooterrental.application.user.query;

import com.vlad.scooterrental.application.auth.UserView;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetCurrentUserQueryHandler {

    private final UserReadRepository userReadRepository;

    public GetCurrentUserQueryHandler(UserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    public UserView handle(UUID userId) {
        return userReadRepository.findViewById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
