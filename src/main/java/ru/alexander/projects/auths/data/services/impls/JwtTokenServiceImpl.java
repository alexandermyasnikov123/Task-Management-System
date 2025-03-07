package ru.alexander.projects.auths.data.services.impls;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.auths.infrastructure.configurations.JwtClaims;
import ru.alexander.projects.auths.utils.UserDetailsUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenServiceImpl implements JwtTokenService {
    public static final String CREATION_EXCEPTION_MESSAGE =
            "Can't create token for non existing or inactive user. Check input again";

    UserDetailsManager userDetailsManager;

    JwtClaims jwtClaims;

    @Override
    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    @Override
    public String createToken(String username, String role) {
        if (!verifyUserIsValid(username, role)) {
            throw new IllegalArgumentException(CREATION_EXCEPTION_MESSAGE);
        }

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
                    .withClaim(USERNAME_KEY, (usernameClaim, decodedToken) ->
                            verifyUserIsValid(usernameClaim.asString(), decodedToken.getClaim(ROLE_KEY).asString())
                    ).build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private boolean verifyUserIsValid(String username, String role) {
        try {
            final var user = userDetailsManager.loadUserByUsername(username);
            return UserDetailsUtils.isUserActive(user) && user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> authority.equalsIgnoreCase(role));
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }
}
