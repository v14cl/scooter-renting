package com.vlad.scooterrental.domain.repository;

import com.vlad.scooterrental.domain.model.User;

public interface TokenProvider {

  String issueToken(User user);
}
