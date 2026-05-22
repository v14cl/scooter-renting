package com.vlad.scooterrental.infrastructure.persistence.mapper;

import com.vlad.scooterrental.domain.model.Role;
import com.vlad.scooterrental.domain.model.User;
import com.vlad.scooterrental.domain.value.Email;
import com.vlad.scooterrental.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

  public User toDomain(UserEntity entity) {
    return new User(
        entity.getId(),
        entity.getFullName(),
        Email.of(entity.getEmail()),
        entity.getPasswordHash(),
        Role.valueOf(entity.getRole()));
  }

  public UserEntity toEntity(User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.id());
    entity.setFullName(user.fullName());
    entity.setEmail(user.email().value());
    entity.setPasswordHash(user.passwordHash());
    entity.setRole(user.role().name());
    return entity;
  }
}
