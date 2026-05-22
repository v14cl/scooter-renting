package com.vlad.scooterrental.application.auth;

import com.vlad.scooterrental.application.user.query.UserView;

public record AuthResult(String token, UserView user) {}
