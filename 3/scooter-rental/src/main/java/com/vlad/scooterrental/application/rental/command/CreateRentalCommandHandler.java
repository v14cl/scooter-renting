package com.vlad.scooterrental.application.rental.command;

import com.vlad.scooterrental.application.rental.query.RentalView;
import com.vlad.scooterrental.domain.factory.RentalFactory;
import com.vlad.scooterrental.domain.repository.RentalRepository;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateRentalCommandHandler {

    private final RentalFactory rentalFactory;
    private final RentalRepository rentalRepository;
    private final ScooterRepository scooterRepository;

    public CreateRentalCommandHandler(
            RentalFactory rentalFactory,
            RentalRepository rentalRepository,
            ScooterRepository scooterRepository
    ) {
        this.rentalFactory = rentalFactory;
        this.rentalRepository = rentalRepository;
        this.scooterRepository = scooterRepository;
    }

    public RentalView handle(CreateRentalCommand command) {
        var rental = rentalFactory.create(
                command.actor().userId(),
                command.scooterId(),
                command.startTime(),
                command.endTime(),
                rentalRepository,
                scooterRepository
        );
        return RentalView.from(rentalRepository.save(rental));
    }
}
