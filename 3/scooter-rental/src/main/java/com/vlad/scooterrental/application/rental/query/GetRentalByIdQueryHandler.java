package com.vlad.scooterrental.application.rental.query;

import com.vlad.scooterrental.application.common.Actor;
import com.vlad.scooterrental.domain.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetRentalByIdQueryHandler {

    private final RentalReadRepository rentalReadRepository;

    public GetRentalByIdQueryHandler(RentalReadRepository rentalReadRepository) {
        this.rentalReadRepository = rentalReadRepository;
    }

    public RentalView handle(UUID rentalId, Actor actor) {
        return rentalReadRepository.findVisibleById(rentalId, actor)
                .orElseThrow(() -> new NotFoundException("Rental not found"));
    }
}
