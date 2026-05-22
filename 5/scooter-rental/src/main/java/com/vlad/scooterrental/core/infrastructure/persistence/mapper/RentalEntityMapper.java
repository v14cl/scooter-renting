package com.vlad.scooterrental.core.infrastructure.persistence.mapper;

import com.vlad.scooterrental.core.domain.model.Rental;
import com.vlad.scooterrental.core.domain.value.RentalPeriod;
import com.vlad.scooterrental.core.infrastructure.persistence.entity.RentalEntity;
import org.springframework.stereotype.Component;

@Component
public class RentalEntityMapper {

  public Rental toDomain(RentalEntity entity) {
    return new Rental(
        entity.getId(),
        entity.getRenterId(),
        entity.getScooterId(),
        RentalPeriod.restore(entity.getStartTime(), entity.getEndTime()));
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
