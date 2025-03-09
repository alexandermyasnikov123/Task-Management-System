package ru.alexander.projects.auths.domain.services;

import ru.alexander.projects.auths.domain.models.requests.CreateUserRequest;
import ru.alexander.projects.auths.domain.models.responses.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse findUserByEmail(String email);

    UserResponse findUserByUsername(String username);

    void deleteUser(String username);
}
