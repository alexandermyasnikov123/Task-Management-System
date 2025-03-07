package ru.alexander.projects.auths.data.services.impls;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.auths.infrastructure.configurations.JwtClaims;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenServiceImpl implements JwtTokenService {
    JwtClaims jwtClaims;

    @Override
    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    @Override
    public String extractRole(String token) {
        return JWT.decode(token).getClaim(ROLE_KEY).asString();
    }

    @Override
    public String createToken(String username, String role) {
        final var issuedAt = Instant.now();
        return JWT.create()
                .withIssuer(jwtClaims.getIssuer())
                .withIssuedAt(issuedAt)
                .withExpiresAt(issuedAt.plus(jwtClaims.getExpirationMillis(), ChronoUnit.MILLIS))
                .withSubject(username)
                .withClaim(USERNAME_KEY, username)
                .withClaim(ROLE_KEY, role)
                .sign(jwtClaims.getAlgorithm());
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            final var verifier = JWT.require(jwtClaims.getAlgorithm())
                    .withIssuer(jwtClaims.getIssuer())
                    .withClaim(USERNAME_KEY, extractUsername(token))
                    .withClaim(ROLE_KEY, extractRole(token))
                    .build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
