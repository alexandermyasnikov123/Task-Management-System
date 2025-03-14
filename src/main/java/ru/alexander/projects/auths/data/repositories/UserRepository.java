package ru.alexander.projects.auths.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexander.projects.auths.data.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsernameOrEmail (String username, String email);

    void deleteByUsername(String username);
}
