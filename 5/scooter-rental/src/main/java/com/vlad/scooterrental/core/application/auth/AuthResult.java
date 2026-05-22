package com.vlad.scooterrental.core.application.auth;

import com.vlad.scooterrental.core.application.user.query.UserView;

public record AuthResult(String token, UserView user) {}
