package com.vlad.scooterrental.application.scooter.query;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListScootersQueryHandler {

    private final ScooterReadRepository scooterReadRepository;

    public ListScootersQueryHandler(ScooterReadRepository scooterReadRepository) {
        this.scooterReadRepository = scooterReadRepository;
    }

    public List<ScooterView> handle() {
        return scooterReadRepository.findAllViews();
    }
}
