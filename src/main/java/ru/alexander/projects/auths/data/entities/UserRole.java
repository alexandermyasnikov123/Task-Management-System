package ru.alexander.projects.auths.data.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    ADMIN, COMMON_USER;

    public static final String ROLE_PREFIX = "ROLE_";

    public GrantedAuthority toRoleAuthority() {
        return new SimpleGrantedAuthority(ROLE_PREFIX + name());
    }
}
