package ru.alexander.projects.auths.infrastructure.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
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
import org.springframework.web.util.WebUtils;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.shared.utils.UserUtils;

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
        final var cookieToken = Optional.ofNullable(WebUtils.getCookie(request, JwtTokenService.TOKEN_COOKIE))
                .map(Cookie::getValue);

        final var headerToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .map(headerValue -> headerValue.substring(JwtTokenService.TOKEN_PREFIX.length()));

        final var bearerToken = cookieToken.or(() -> headerToken);

        if (!shouldUserBeAccessed(bearerToken)) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private boolean shouldUserBeAccessed(Optional<String> optionalToken) {
        return optionalToken.map(token -> {
            if (!jwtTokenService.isTokenValid(token)) {
                return false;
            }

            try {
                final var username = jwtTokenService.extractUsername(token);
                final var userDetails = userDetailsService.loadUserByUsername(username);
                final var authorities = userDetails.getAuthorities();

                return UserUtils.isActive(userDetails) && UserUtils.authenticate(username, null, authorities);
            } catch (UsernameNotFoundException e) {
                return false;
            }
        }).orElse(true);
    }
}
