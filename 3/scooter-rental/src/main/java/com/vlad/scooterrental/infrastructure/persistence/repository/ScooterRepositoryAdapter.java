package com.vlad.scooterrental.infrastructure.persistence.repository;

import com.vlad.scooterrental.domain.model.Scooter;
import com.vlad.scooterrental.domain.repository.ScooterRepository;
import com.vlad.scooterrental.infrastructure.persistence.jpa.ScooterJpaRepository;
import com.vlad.scooterrental.infrastructure.persistence.mapper.ScooterEntityMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ScooterRepositoryAdapter implements ScooterRepository {

    private final ScooterJpaRepository scooterJpaRepository;
    private final ScooterEntityMapper scooterEntityMapper;

    public ScooterRepositoryAdapter(ScooterJpaRepository scooterJpaRepository, ScooterEntityMapper scooterEntityMapper) {
        this.scooterJpaRepository = scooterJpaRepository;
        this.scooterEntityMapper = scooterEntityMapper;
    }

    @Override
    public Optional<Scooter> findById(UUID id) {
        return scooterJpaRepository.findById(id).map(scooterEntityMapper::toDomain);
    }

    @Override
    public List<Scooter> findAll() {
        return scooterJpaRepository.findAll(Sort.by("code").ascending()).stream()
                .map(scooterEntityMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCode(String code) {
        return scooterJpaRepository.existsByCode(code);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, UUID id) {
        return scooterJpaRepository.existsByCodeAndIdNot(code, id);
    }

    @Override
    public Scooter save(Scooter scooter) {
        return scooterEntityMapper.toDomain(scooterJpaRepository.save(scooterEntityMapper.toEntity(scooter)));
    }

    @Override
    public void deleteById(UUID id) {
        scooterJpaRepository.deleteById(id);
    }
}
