package ru.alexander.projects.auths.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Every utils class I declare can't be instantiated using constructor
 * or be inherited. I don't like to use @lombok.experimental.UtilityClass
 * because it can cause unexpected issues (like static imports).
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDetailsUtils {

    public static boolean isUserActive(UserDetails userDetails) {
        final var isNotExpired = userDetails.isAccountNonExpired() && userDetails.isCredentialsNonExpired();
        final var isActiveUser = userDetails.isAccountNonLocked() && userDetails.isEnabled();
        return isNotExpired && isActiveUser;
    }
}
