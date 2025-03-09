package ru.alexander.projects.auths.domain.services;

public interface JwtTokenService {
    String TOKEN_HEADER = "JWT-TOKEN";

    String TOKEN_PREFIX = "Bearer ";

    String USERNAME_KEY = "username";

    String ROLE_KEY = "role";

    String extractUsername(String token);

    String extractRole(String token);

    String createToken(String username, String role);

    boolean isTokenValid(String token);
}
