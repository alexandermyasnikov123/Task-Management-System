package ru.alexander.projects.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import ru.alexander.projects.auths.data.entities.IdentifiableUserDetails;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserUtils {

    public static boolean isActive(UserDetails userDetails) {
        final var isNotExpired = userDetails.isAccountNonExpired() && userDetails.isCredentialsNonExpired();
        final var isActiveUser = userDetails.isAccountNonLocked() && userDetails.isEnabled();
        return isNotExpired && isActiveUser;
    }

    public static boolean authenticate(IdentifiableUserDetails<?> userDetails, boolean ignorePassword) {
        final var request = ServletUtils.getCurrentRequest().orElseThrow();

        final var context = SecurityContextHolder.createEmptyContext();
        final var authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                ignorePassword ? null : userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        return authToken.isAuthenticated();
    }

    @SuppressWarnings("unchecked")
    public static <T extends IdentifiableUserDetails<?>> Optional<T> getPrincipal() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> Objects.requireNonNull((T) authentication.getPrincipal()));
    }

    public static Optional<String> getPrincipalUsername() {
        return getPrincipal().map(UserDetails::getUsername);
    }

    public static Optional<UUID> getPrincipalId() {
        return UserUtils.<IdentifiableUserDetails<UUID>>getPrincipal()
                .map(IdentifiableUserDetails::getId);
    }
}
