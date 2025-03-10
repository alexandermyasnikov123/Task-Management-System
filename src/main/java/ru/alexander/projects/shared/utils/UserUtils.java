package ru.alexander.projects.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserUtils {

    public static boolean isActive(UserDetails userDetails) {
        final var isNotExpired = userDetails.isAccountNonExpired() && userDetails.isCredentialsNonExpired();
        final var isActiveUser = userDetails.isAccountNonLocked() && userDetails.isEnabled();
        return isNotExpired && isActiveUser;
    }

    public static boolean authenticate(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        final var request = ServletUtils.getCurrentRequest().orElseThrow();

        final var context = SecurityContextHolder.createEmptyContext();
        final var authToken = new UsernamePasswordAuthenticationToken(username, password, authorities);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        return authToken.isAuthenticated();
    }

    public static Optional<String> getPrincipalUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> Objects.requireNonNull((String) authentication.getPrincipal()));
    }
}
