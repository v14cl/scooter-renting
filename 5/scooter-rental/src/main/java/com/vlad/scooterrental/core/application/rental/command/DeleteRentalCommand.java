package com.vlad.scooterrental.core.application.rental.command;

import com.vlad.scooterrental.core.application.common.Actor;
import java.util.UUID;

public record DeleteRentalCommand(Actor actor, UUID rentalId) {}
