package com.vlad.scooterrental.core.infrastructure.security;

import com.vlad.scooterrental.core.domain.model.User;
import com.vlad.scooterrental.core.domain.repository.TokenProvider;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {

  private final JwtEncoder jwtEncoder;
  private final long expirationSeconds;

  public JwtTokenProvider(
      JwtEncoder jwtEncoder,
      @Value("${app.security.jwt.expiration-seconds}") long expirationSeconds) {
    this.jwtEncoder = jwtEncoder;
    this.expirationSeconds = expirationSeconds;
  }

  @Override
  public String issueToken(User user) {
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .subject(user.id().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expirationSeconds))
            .claim("roles", List.of(user.role().name()))
            .claim("email", user.email().value())
            .build();
    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
