package ru.alexander.projects.auths.domain.models.responses;

import java.util.UUID;

public record UserResponse(UUID id, String username, String role) {
}
