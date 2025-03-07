package ru.alexander.projects.auths.infrastructure.configurations;

import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "auths.jwt.claims")
public class JwtClaims {
    String secret;

    String issuer;

    Long expirationMillis;

    Algorithm algorithm;

    @PostConstruct
    public void initialize() {
        algorithm = Algorithm.HMAC512(secret);
    }
}
