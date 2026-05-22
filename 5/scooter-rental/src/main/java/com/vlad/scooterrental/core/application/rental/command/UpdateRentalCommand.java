package com.vlad.scooterrental.core.application.rental.command;

import com.vlad.scooterrental.core.application.common.Actor;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateRentalCommand(
    Actor actor, UUID rentalId, UUID scooterId, LocalDateTime startTime, LocalDateTime endTime) {}
