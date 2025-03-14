package ru.alexander.projects.auths.domain.models.responses;

import java.util.UUID;

public record AuthResponse(UUID id, String username, String role, String jwtToken) {
}
