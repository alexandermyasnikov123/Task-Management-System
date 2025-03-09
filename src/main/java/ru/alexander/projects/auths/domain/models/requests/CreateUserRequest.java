package ru.alexander.projects.auths.domain.models.requests;

import ru.alexander.projects.auths.data.entities.UserRole;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        UserRole role
) {
}
