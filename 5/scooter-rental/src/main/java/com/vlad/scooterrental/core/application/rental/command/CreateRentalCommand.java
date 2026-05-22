package com.vlad.scooterrental.core.application.rental.command;

import com.vlad.scooterrental.core.application.common.Actor;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateRentalCommand(
    Actor actor, UUID scooterId, LocalDateTime startTime, LocalDateTime endTime) {}
