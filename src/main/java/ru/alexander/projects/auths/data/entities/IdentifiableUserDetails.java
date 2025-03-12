package ru.alexander.projects.auths.data.entities;

import org.springframework.security.core.userdetails.UserDetails;

public interface IdentifiableUserDetails<T> extends UserDetails {
    T getId();
}
