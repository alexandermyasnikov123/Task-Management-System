package ru.alexander.projects.auths.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexander.projects.auths.data.entities.UserDetailsEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, UUID> {
    Optional<UserDetailsEntity> findByUsername(String username);

    Optional<UserDetailsEntity> findByEmail(String email);

    Optional<UserDetailsEntity> findByUsernameOrEmail (String username, String email);

    void deleteByUsername(String username);
}
