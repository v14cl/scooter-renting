package com.vlad.scooterrental.application.scooter.query;

import com.vlad.scooterrental.domain.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetScooterByIdQueryHandler {

    private final ScooterReadRepository scooterReadRepository;

    public GetScooterByIdQueryHandler(ScooterReadRepository scooterReadRepository) {
        this.scooterReadRepository = scooterReadRepository;
    }

    public ScooterView handle(UUID scooterId) {
        return scooterReadRepository.findViewById(scooterId)
                .orElseThrow(() -> new NotFoundException("Scooter not found"));
    }
}
