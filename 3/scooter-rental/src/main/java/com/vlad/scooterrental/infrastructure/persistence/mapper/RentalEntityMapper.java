package com.vlad.scooterrental.infrastructure.persistence.mapper;

import com.vlad.scooterrental.domain.model.Rental;
import com.vlad.scooterrental.domain.value.RentalPeriod;
import com.vlad.scooterrental.infrastructure.persistence.entity.RentalEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class RentalEntityMapper {

    private final Clock clock;

    public RentalEntityMapper(Clock clock) {
        this.clock = clock;
    }

    public Rental toDomain(RentalEntity entity) {
        return new Rental(
                entity.getId(),
                entity.getRenterId(),
                entity.getScooterId(),
                RentalPeriod.restore(entity.getStartTime(), entity.getEndTime())
        );
    }

    public RentalEntity toEntity(Rental rental) {
        RentalEntity entity = new RentalEntity();
        entity.setId(rental.id());
        entity.setRenterId(rental.renterId());
        entity.setScooterId(rental.scooterId());
        entity.setStartTime(rental.period().startTime());
        entity.setEndTime(rental.period().endTime());
        return entity;
    }
}
