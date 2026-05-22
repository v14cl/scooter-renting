package com.vlad.scooterrental.infrastructure.persistence.jpa;

import com.vlad.scooterrental.infrastructure.persistence.entity.ScooterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScooterJpaRepository extends JpaRepository<ScooterEntity, UUID> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, UUID id);
}
