package com.vlad.scooterrental.application.rental.query;

import com.vlad.scooterrental.application.common.Actor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListRentalsQueryHandler {

    private final RentalReadRepository rentalReadRepository;

    public ListRentalsQueryHandler(RentalReadRepository rentalReadRepository) {
        this.rentalReadRepository = rentalReadRepository;
    }

    public List<RentalView> handle(Actor actor) {
        return rentalReadRepository.findAllVisibleFor(actor);
    }
}
