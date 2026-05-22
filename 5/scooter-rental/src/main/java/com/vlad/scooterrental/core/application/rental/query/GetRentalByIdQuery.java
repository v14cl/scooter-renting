package com.vlad.scooterrental.core.application.rental.query;

import com.vlad.scooterrental.core.application.common.Actor;
import java.util.UUID;

public record GetRentalByIdQuery(UUID rentalId, Actor actor) {}
