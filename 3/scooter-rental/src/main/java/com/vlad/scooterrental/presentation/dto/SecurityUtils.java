package com.vlad.scooterrental.presentation.dto;

import com.vlad.scooterrental.application.common.Actor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Actor actor(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        List<String> roles = jwt.getClaimAsStringList("roles");
        return Actor.fromClaims(UUID.fromString(jwt.getSubject()), roles.get(0));
    }

    public static UUID userId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return UUID.fromString(jwt.getSubject());
    }
}
