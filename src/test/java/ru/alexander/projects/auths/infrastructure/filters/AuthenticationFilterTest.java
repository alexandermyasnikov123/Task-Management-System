package ru.alexander.projects.auths.infrastructure.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.auths.utils.UserDetailsUtils;
import ru.alexander.projects.base.tests.BaseUnitTest;

@FieldDefaults(level = AccessLevel.PRIVATE)
class AuthenticationFilterTest extends BaseUnitTest {
    @Mock
    JwtTokenService jwtTokenService;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    HttpServletRequest servletRequest;

    @Mock
    HttpServletResponse servletResponse;

    @Mock
    FilterChain filterChain;

    @Mock
    MockedStatic<UserDetailsUtils> userDetailsUtils;

    AuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AuthenticationFilter(jwtTokenService, userDetailsService);
    }

    @Test
    @DisplayName(value = "Filter accepts empty Authorization header")
    @SneakyThrows
    void testFilterEmptyHeader() {
        Mockito.when(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        filter.doFilterInternal(servletRequest, servletResponse, filterChain);

        Mockito.verify(filterChain).doFilter(servletRequest, servletResponse);

        Mockito.verify(jwtTokenService, Mockito.never()).isTokenValid(Mockito.any());
        Mockito.verify(userDetailsService, Mockito.never()).loadUserByUsername(Mockito.any());

        userDetailsUtils.verify(() -> UserDetailsUtils.isActive(Mockito.any()), Mockito.never());
        userDetailsUtils.verify(() -> UserDetailsUtils.authenticate(Mockito.any(), Mockito.any()), Mockito.never());
    }

    @Test
    @DisplayName(value = "Filter accepts correct Bearer tokens")
    @SneakyThrows
    void testFilterCorrectJwt() {
        final var token = "correct-token";
        final var headerValue = JwtTokenService.TOKEN_PREFIX + token;

        Mockito.when(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(headerValue);
        Mockito.when(jwtTokenService.isTokenValid(token)).thenReturn(true);

        userDetailsUtils.when(() -> UserDetailsUtils.isActive(Mockito.any())).thenReturn(true);
        userDetailsUtils.when(() -> UserDetailsUtils.authenticate(Mockito.any(), Mockito.any())).thenReturn(true);

        filter.doFilterInternal(servletRequest, servletResponse, filterChain);

        /// Lazy evaluation testing. If user is not active do not authenticate
        final var inOrder = Mockito.inOrder(UserDetailsUtils.class);
        inOrder.verify(userDetailsUtils, () -> UserDetailsUtils.isActive(Mockito.any()));
        inOrder.verify(userDetailsUtils, () -> UserDetailsUtils.authenticate(Mockito.any(), Mockito.any()));

        Mockito.verify(filterChain).doFilter(servletRequest, servletResponse);
    }

    @Test
    @DisplayName(value = "Filter declines invalid Bearer tokens")
    @SneakyThrows
    void testFilterInvalidJwt() {
        final var token = "correct-token";
        final var headerValue = JwtTokenService.TOKEN_PREFIX + token;

        Mockito.when(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(headerValue);
        Mockito.when(jwtTokenService.isTokenValid(token)).thenReturn(false);

        userDetailsUtils.when(() -> UserDetailsUtils.isActive(Mockito.any())).thenReturn(true);
        userDetailsUtils.when(() -> UserDetailsUtils.authenticate(Mockito.any(), Mockito.any())).thenReturn(true);

        filter.doFilterInternal(servletRequest, servletResponse, filterChain);

        userDetailsUtils.verify(() -> UserDetailsUtils.isActive(Mockito.any()), Mockito.never());
        userDetailsUtils.verify(() -> UserDetailsUtils.authenticate(Mockito.any(), Mockito.any()), Mockito.never());

        Mockito.verify(filterChain, Mockito.never()).doFilter(servletRequest, servletResponse);
    }
}