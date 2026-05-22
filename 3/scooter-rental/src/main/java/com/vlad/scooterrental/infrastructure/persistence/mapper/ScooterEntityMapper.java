package com.vlad.scooterrental.infrastructure.persistence.mapper;

import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.model.ScooterStatus;
import com.vlad.scooterrental.infrastructure.persistence.entity.ScooterEntity;
import org.springframework.stereotype.Component;

@Component
public class ScooterEntityMapper {

    public Scooter toDomain(ScooterEntity entity) {
        return new Scooter(
                entity.getId(),
                entity.getCode(),
                entity.getModel(),
                ScooterStatus.valueOf(entity.getStatus()),
                entity.getBatteryLevel(),
                entity.getPricePerMinute()
        );
    }

    public ScooterEntity toEntity(Scooter scooter) {
        ScooterEntity entity = new ScooterEntity();
        entity.setId(scooter.id());
        entity.setCode(scooter.code());
        entity.setModel(scooter.model());
        entity.setStatus(scooter.status().name());
        entity.setBatteryLevel(scooter.batteryLevel());
        entity.setPricePerMinute(scooter.pricePerMinute());
        return entity;
    }
}
