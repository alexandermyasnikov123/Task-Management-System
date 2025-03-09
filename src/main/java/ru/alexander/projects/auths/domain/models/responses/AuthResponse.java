package ru.alexander.projects.auths.domain.models.responses;

public record AuthResponse(String username, String role, String jwtToken) {
}
