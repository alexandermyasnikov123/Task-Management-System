package ru.alexander.projects.auths.data.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

public enum UserRole {
    ADMIN, USER;

    public static final String ROLE_PREFIX = "ROLE_";

    public static String[] getRoleNames() {
        return Arrays.stream(values())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    public GrantedAuthority toRoleAuthority() {
        return new SimpleGrantedAuthority(ROLE_PREFIX + name());
    }
}
