package ru.alexander.projects.auths.infrastructure.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.shared.utils.UserDetailsUtils;

import java.util.Optional;

@Lazy
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter extends OncePerRequestFilter {
    JwtTokenService jwtTokenService;

    UserDetailsService userDetailsService;

    @Override
    @SneakyThrows
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) {
        if (shouldUserBeAccessed(request)) {
            filterChain.doFilter(request, response);
        } else {
            SecurityContextHolder.clearContext();
        }
    }

    private boolean shouldUserBeAccessed(HttpServletRequest request) {
        final var authorizationHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));

        return authorizationHeader.map(headerValue -> {
            final var token = headerValue.substring(JwtTokenService.TOKEN_PREFIX.length());
            final var isTokenValid = jwtTokenService.isTokenValid(token);

            try {
                if (isTokenValid) {
                    final var username = jwtTokenService.extractUsername(token);
                    final var userDetails = userDetailsService.loadUserByUsername(username);

                    return UserDetailsUtils.isActive(userDetails) && UserDetailsUtils.authenticate(userDetails, request);
                }
            } catch (UsernameNotFoundException ignored) {
            }
            return false;
        }).orElse(true);
    }
}
