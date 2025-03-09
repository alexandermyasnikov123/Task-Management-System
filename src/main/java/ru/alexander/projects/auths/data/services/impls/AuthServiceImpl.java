package ru.alexander.projects.auths.data.services.impls;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.alexander.projects.auths.data.entities.UserRole;
import ru.alexander.projects.auths.data.mappers.UserMapper;
import ru.alexander.projects.auths.domain.models.requests.AuthRequest;
import ru.alexander.projects.auths.domain.models.requests.RegisterRequest;
import ru.alexander.projects.auths.domain.models.responses.AuthResponse;
import ru.alexander.projects.auths.domain.services.AuthService;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.auths.domain.services.UserService;
import ru.alexander.projects.shared.utils.UserUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserService userService;

    JwtTokenService jwtTokenService;

    AuthenticationManager authenticationManager;

    UserMapper mapper;

    @Override
    public AuthResponse registerUser(RegisterRequest request) {
        final var createUserRequest = mapper.mapToCreateRequest(request);
        final var userResponse = userService.createUser(createUserRequest);
        final var jwtToken = jwtTokenService.createToken(request.getUsername(), request.getRole());

        return mapper.mapToAuthResponse(userResponse, jwtToken);
    }

    @Override
    public AuthResponse loginUser(AuthRequest request) {
        final var userResponse = userService.findUserByEmail(request.getEmail());
        final var jwtToken = jwtTokenService.createToken(userResponse.username(), userResponse.role());
        final var authorities = List.of(UserRole.valueOf(userResponse.role()).toRoleAuthority());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userResponse.username(), request.getPassword(), authorities)
        );

        return mapper.mapToAuthResponse(userResponse, jwtToken);
    }

    @Override
    public void deleteAuth() {
        final var username = UserUtils.getPrincipalUsername().orElseThrow();

        userService.deleteUser(username);
        SecurityContextHolder.clearContext();
    }
}
