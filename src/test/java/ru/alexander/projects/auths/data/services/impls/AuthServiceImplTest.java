package ru.alexander.projects.auths.data.services.impls;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.alexander.projects.auths.data.entities.UserRole;
import ru.alexander.projects.auths.data.mappers.UserMapper;
import ru.alexander.projects.auths.domain.models.requests.AuthRequest;
import ru.alexander.projects.auths.domain.models.requests.RegisterRequest;
import ru.alexander.projects.auths.domain.models.responses.UserResponse;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.auths.domain.services.UserService;
import ru.alexander.projects.base.tests.BaseUnitTest;
import ru.alexander.projects.shared.utils.UserUtils;

import java.util.Optional;

import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class AuthServiceImplTest extends BaseUnitTest {
    @Mock
    UserService userService;

    @Mock
    JwtTokenService jwtTokenService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    MockedStatic<UserUtils> userUtils;

    @Mock
    MockedStatic<SecurityContextHolder> securityContextHolder;

    @Spy
    UserMapper mapper;

    AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(userService, jwtTokenService, authenticationManager, mapper);
    }

    @Test
    @DisplayName(value = "registerUser creates user and token")
    void testRegisterUser() {
        authService.registerUser(new RegisterRequest(null, null, null, null));

        final var inOrder = inOrder(mapper, userService, jwtTokenService);
        inOrder.verify(mapper).mapToCreateRequest(any());
        inOrder.verify(userService).createUser(any());
        inOrder.verify(jwtTokenService).createToken(any(), any());
        inOrder.verify(mapper).mapToAuthResponse(any(), any());
    }

    @Test
    @DisplayName(value = "loginUser finds user and associate him with a token")
    void testLoginUser() {
        final var user = mock(UserResponse.class);
        when(userService.findUserByEmail(any())).thenReturn(user);
        when(user.role()).thenReturn(UserRole.COMMON_USER.name());

        authService.loginUser(new AuthRequest(null, null));

        final var inOrder = inOrder(mapper, userService, jwtTokenService, authenticationManager);
        inOrder.verify(userService).findUserByEmail(any());
        inOrder.verify(jwtTokenService).createToken(any(), any());
        inOrder.verify(authenticationManager).authenticate(any());
        inOrder.verify(mapper).mapToAuthResponse(any(), any());
    }

    @Test
    @DisplayName(value = "deleteAuth clears current principal from the context")
    void testDeleteAuth() {
        userUtils.when(UserUtils::getPrincipalUsername).thenReturn(Optional.of("any-username"));

        authService.deleteAuth();

        final var inOrder = inOrder(userService, UserUtils.class, SecurityContextHolder.class);
        inOrder.verify(userUtils, UserUtils::getPrincipalUsername);
        inOrder.verify(userService).deleteUser(any());
        inOrder.verify(securityContextHolder, SecurityContextHolder::clearContext);
    }
}