package ru.alexander.projects.auths.domain.services;

import org.springframework.security.web.csrf.CsrfToken;
import ru.alexander.projects.auths.domain.models.requests.AuthRequest;
import ru.alexander.projects.auths.domain.models.requests.RegisterRequest;
import ru.alexander.projects.auths.domain.models.responses.AuthResponse;

public interface AuthService {
    AuthResponse registerUser(RegisterRequest request);

    AuthResponse loginUser(AuthRequest request);

    void deleteAuth();
}
