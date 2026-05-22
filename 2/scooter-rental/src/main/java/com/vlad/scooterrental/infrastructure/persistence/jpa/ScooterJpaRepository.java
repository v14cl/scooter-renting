package com.vlad.scooterrental.infrastructure.persistence.jpa;

import com.vlad.scooterrental.infrastructure.persistence.entity.ScooterEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScooterJpaRepository extends JpaRepository<ScooterEntity, UUID> {

  boolean existsByCode(String code);

  boolean existsByCodeAndIdNot(String code, UUID id);
}
