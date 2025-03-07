package ru.alexander.projects.auths.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDetailsUtils {

    public static boolean isActive(UserDetails userDetails) {
        final var isNotExpired = userDetails.isAccountNonExpired() && userDetails.isCredentialsNonExpired();
        final var isActiveUser = userDetails.isAccountNonLocked() && userDetails.isEnabled();
        return isNotExpired && isActiveUser;
    }

    public static boolean authenticate(UserDetails userDetails, HttpServletRequest request) {
        final var context = SecurityContextHolder.createEmptyContext();
        final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        return authToken.isAuthenticated();
    }

    public static Optional<UserDetails> getCurrentPrincipal() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> Objects.requireNonNull((UserDetails) authentication.getPrincipal()));
    }
}
