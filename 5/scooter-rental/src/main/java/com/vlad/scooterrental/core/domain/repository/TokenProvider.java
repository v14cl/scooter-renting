package com.vlad.scooterrental.core.domain.repository;

import com.vlad.scooterrental.core.domain.model.User;

public interface TokenProvider {

  String issueToken(User user);
}
