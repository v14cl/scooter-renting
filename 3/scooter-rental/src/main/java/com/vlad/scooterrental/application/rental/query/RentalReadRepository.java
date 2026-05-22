package com.vlad.scooterrental.application.rental.query;

import com.vlad.scooterrental.application.common.Actor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalReadRepository {

    List<RentalView> findAllVisibleFor(Actor actor);

    Optional<RentalView> findVisibleById(UUID rentalId, Actor actor);
}
