package com.vlad.scooterrental.infrastructure.persistence.repository;

import com.vlad.scooterrental.domain.model.User;
import com.vlad.scooterrental.domain.repository.UserRepository;
import com.vlad.scooterrental.domain.value.Email;
import com.vlad.scooterrental.infrastructure.persistence.jpa.UserJpaRepository;
import com.vlad.scooterrental.infrastructure.persistence.mapper.UserEntityMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, UserEntityMapper userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userJpaRepository.findByEmail(email.value()).map(userEntityMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userJpaRepository.existsByEmail(email.value());
    }

    @Override
    public boolean existsByEmailAndIdNot(Email email, UUID id) {
        return userJpaRepository.existsByEmailAndIdNot(email.value(), id);
    }

    @Override
    public User save(User user) {
        return userEntityMapper.toDomain(userJpaRepository.save(userEntityMapper.toEntity(user)));
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll(Sort.by("fullName").ascending()).stream()
                .map(userEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }
}
