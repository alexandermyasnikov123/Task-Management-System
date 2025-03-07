package ru.alexander.projects.auths.domain.services;

public interface JwtTokenService {
    String TOKEN_PREFIX = "Bearer ";

    String USERNAME_KEY = "username";

    String ROLE_KEY = "role";

    String extractUsername(String token);

    /**
     * @throws IllegalArgumentException The exception is thrown in case when
     *                                  can't find a user with the actual username or its role is not the same
     *                                  as supposing argument.
     */
    String createToken(String username, String role) throws IllegalArgumentException;

    /**
     * @return true if the token is not expired
     * and there is a user with the actual claims,
     * false otherwise.
     */
    boolean isTokenValid(String token);
}
