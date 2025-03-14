package ru.alexander.projects.auths.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexander.projects.auths.domain.models.requests.AuthRequest;
import ru.alexander.projects.auths.domain.models.requests.RegisterRequest;
import ru.alexander.projects.auths.domain.models.responses.AuthResponse;
import ru.alexander.projects.auths.domain.services.AuthService;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.shared.utils.ServletUtils;

import java.util.function.Supplier;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(
            @Valid
            @RequestBody
            RegisterRequest request,
            HttpServletResponse response
    ) {
        return authAndSaveTokenCookie(response, () -> authService.registerUser(request));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> loginUser(
            @Valid
            @RequestBody
            AuthRequest request,
            HttpServletResponse response
    ) {
        return authAndSaveTokenCookie(response, () -> authService.loginUser(request));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCurrentUser() {
        authService.deleteAuth();
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> authAndSaveTokenCookie(
            HttpServletResponse servletResponse,
            Supplier<AuthResponse> responseSupplier
    ) {
        final var data = responseSupplier.get();
        ServletUtils.addBasePathCookie(servletResponse, JwtTokenService.TOKEN_COOKIE, data.jwtToken());
        return ResponseEntity.ok(data);
    }
}
