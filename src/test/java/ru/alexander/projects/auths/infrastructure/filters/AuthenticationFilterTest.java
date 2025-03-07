package ru.alexander.projects.auths.infrastructure.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.verification.VerificationMode;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.auths.utils.UserDetailsUtils;
import ru.alexander.projects.base.tests.BaseUnitTest;

import java.util.function.Function;

import static org.mockito.Mockito.*;

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
    void testFilterEmptyHeader() {
        final var context = new TokenContext()
                .token(null)
                .tokenPrefix(null)
                .shouldTokenWillBeChecked(false)
                .shouldIsUserActiveWillBeChecked(false)
                .shouldUserSuccessAuthenticatedWillBeChecked(false)
                .shouldFilterChainBeCalled(true);

        context.test();
    }

    @Test
    @DisplayName(value = "Filter accepts correct Bearer tokens")
    void testFilterCorrectJwt() {
        final var context = new TokenContext()
                .token("correct-jwt-token")
                .isTokenValid(true)
                .shouldTokenWillBeChecked(true)
                .isUserActive(true)
                .shouldIsUserActiveWillBeChecked(true)
                .isSuccessAuthenticated(true)
                .shouldUserSuccessAuthenticatedWillBeChecked(true)
                .shouldFilterChainBeCalled(true);

        context.test();
    }

    @Test
    @DisplayName(value = "Filter declines invalid Bearer tokens")
    void testFilterInvalidJwt() {
        final var context = new TokenContext()
                .token("invalid-jwt-token")
                .shouldTokenWillBeChecked(true)
                .shouldIsUserActiveWillBeChecked(false)
                .shouldUserSuccessAuthenticatedWillBeChecked(false)
                .shouldFilterChainBeCalled(false);

        context.test();
    }

    @Test
    @DisplayName(value = "Filter declines valid Bearer tokens for non-existing users")
    void testFilterValidJwtNonExistingUsername() {
        final var context = new TokenContext()
                .token("correct-jwt-token")
                .isTokenValid(true)
                .shouldTokenWillBeChecked(true)
                .shouldNotFoundUserWithUsername(true)
                .shouldIsUserActiveWillBeChecked(false)
                .shouldUserSuccessAuthenticatedWillBeChecked(false)
                .shouldFilterChainBeCalled(false);

        context.test();
    }

    @Test
    @DisplayName(value = "Filter declines valid Bearer tokens for inactive users")
    void testFilterValidJwtInactiveUser() {
        final var context = new TokenContext()
                .token("correct-jwt-token")
                .isTokenValid(true)
                .shouldTokenWillBeChecked(true)
                .isUserActive(false)
                .shouldIsUserActiveWillBeChecked(true)
                .shouldUserSuccessAuthenticatedWillBeChecked(false)
                .shouldFilterChainBeCalled(false);

        context.test();
    }

    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private class TokenContext {
        String tokenPrefix = JwtTokenService.TOKEN_PREFIX;

        String token;

        boolean isTokenValid;

        boolean shouldTokenWillBeChecked;

        boolean isUserActive;

        boolean shouldIsUserActiveWillBeChecked;

        boolean isSuccessAuthenticated;

        boolean shouldUserSuccessAuthenticatedWillBeChecked;

        boolean shouldNotFoundUserWithUsername;

        boolean shouldFilterChainBeCalled;

        @SneakyThrows
        void test() {
            final var headerValue = StringUtils.stripToNull(StringUtils.join(tokenPrefix, token));
            final Function<Boolean, VerificationMode> verification = condition -> condition ? times(1) : never();

            when(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(headerValue);
            when(jwtTokenService.isTokenValid(token)).thenReturn(isTokenValid);

            if (shouldNotFoundUserWithUsername) {
                when(userDetailsService.loadUserByUsername(any())).thenThrow(UsernameNotFoundException.class);
            }

            userDetailsUtils.when(() -> UserDetailsUtils.isActive(any())).thenReturn(isUserActive);
            userDetailsUtils.when(() -> UserDetailsUtils.authenticate(any(), any())).thenReturn(isSuccessAuthenticated);

            filter.doFilterInternal(servletRequest, servletResponse, filterChain);

            /// Check lazy evaluation right ordering.
            final var inOrder = inOrder(UserDetailsUtils.class);
            inOrder.verify(userDetailsUtils, () -> UserDetailsUtils.isActive(any()), verification.apply(shouldIsUserActiveWillBeChecked));
            inOrder.verify(userDetailsUtils, () -> UserDetailsUtils.authenticate(any(), any()), verification.apply(shouldUserSuccessAuthenticatedWillBeChecked));

            verify(jwtTokenService, verification.apply(shouldTokenWillBeChecked)).isTokenValid(token);
            verify(filterChain, verification.apply(shouldFilterChainBeCalled)).doFilter(servletRequest, servletResponse);
        }
    }
}