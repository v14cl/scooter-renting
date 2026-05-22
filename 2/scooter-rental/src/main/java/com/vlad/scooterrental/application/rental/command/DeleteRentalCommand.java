package com.vlad.scooterrental.application.rental.command;

import com.vlad.scooterrental.application.common.Actor;
import java.util.UUID;

public record DeleteRentalCommand(Actor actor, UUID rentalId) {}
