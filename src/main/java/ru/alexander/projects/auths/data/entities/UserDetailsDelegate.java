package ru.alexander.projects.auths.data.entities;

import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record UserDetailsDelegate(
        @Delegate UserEntity userEntity
) implements IdentifiableUserDetails<UUID> {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(userEntity.getRole().toRoleAuthority());
    }
}
