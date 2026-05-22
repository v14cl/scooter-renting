package com.vlad.scooterrental.application.rental.query;

import com.vlad.scooterrental.application.common.Actor;
import java.util.UUID;

public record GetRentalByIdQuery(UUID rentalId, Actor actor) {}
