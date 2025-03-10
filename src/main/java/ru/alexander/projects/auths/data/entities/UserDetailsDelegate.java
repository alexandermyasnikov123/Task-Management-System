package ru.alexander.projects.auths.data.entities;

import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserDetailsDelegate(
        @Delegate UserEntity userEntity
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(userEntity.getRole().toRoleAuthority());
    }
}
