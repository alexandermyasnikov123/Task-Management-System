package ru.alexander.projects.unit.auths.data.services.impls;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.alexander.projects.auths.data.entities.UserEntity;
import ru.alexander.projects.auths.data.exceptions.NotUniqueUserException;
import ru.alexander.projects.auths.data.mappers.UserMapper;
import ru.alexander.projects.auths.data.repositories.UserRepository;
import ru.alexander.projects.auths.data.services.impls.UserServiceImpl;
import ru.alexander.projects.auths.domain.models.requests.CreateUserRequest;
import ru.alexander.projects.unit.tests.BaseUnitTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

class UserServiceImplTest extends BaseUnitTest {
    @Mock
    UserRepository repository;

    @Mock
    UserMapper userMapper;

    @Mock
    UserEntity mockUserDetails;

    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(repository, userMapper);
    }

    @Test
    @DisplayName(value = "createUser saves userDetails under the hood")
    void testCreateUniqueUser() {
        final var context = new ServiceContext()
                .shouldFoundUser(false)
                .shouldThrowsNotUniqueUser(false)
                .shouldSaveUserToRepo(true)
                .shouldCallMapper(true)
                .interaction(() -> userService.createUser(
                        new CreateUserRequest(null, null, null, null))
                );

        context.test();
    }

    @Test
    @DisplayName(value = "createUser throws exception if user with the same username is exists")
    void testCreateNonUniqueUsernameUser() {
        final var sameUsername = "same-username";
        final var context = new ServiceContext()
                .username("same-username")
                .shouldFoundUser(true)
                .shouldThrowsNotUniqueUser(true)
                .shouldSaveUserToRepo(false)
                .shouldCallMapper(false)
                .interaction(() -> userService.createUser(
                        new CreateUserRequest(sameUsername, null, null, null)
                ));

        context.test();
    }

    @Test
    @DisplayName(value = "createUser throws exception if user with the same email is exists")
    void testCreateNonUniqueEmailUser() {
        final var sameEmail = "same@email.smtp";
        final var context = new ServiceContext()
                .email(sameEmail)
                .shouldFoundUser(true)
                .shouldThrowsNotUniqueUser(true)
                .shouldSaveUserToRepo(false)
                .shouldCallMapper(false)
                .interaction(() -> userService.createUser(
                        new CreateUserRequest(null, sameEmail, null, null)
                ));

        context.test();
    }

    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private class ServiceContext {
        String email;

        String username;

        boolean shouldThrowsNotUniqueUser;

        boolean shouldFoundUser;

        boolean shouldSaveUserToRepo;

        boolean shouldCallMapper;

        Runnable interaction;

        void test() {
            when(mockUserDetails.getUsername())
                    .thenReturn(username);

            when(mockUserDetails.getEmail())
                    .thenReturn(email);

            when(repository.findByUsernameOrEmail(any(), any()))
                    .thenReturn(Optional.ofNullable(shouldFoundUser ? mockUserDetails : null));

            if (shouldThrowsNotUniqueUser) {
                assertThrowsExactly(
                        NotUniqueUserException.class,
                        interaction::run,
                        "errors.not-unique-user.details"
                );
            } else {
                interaction.run();
            }

            final var inOrder = inOrder(repository, userMapper);
            inOrder.verify(repository)
                    .findByUsernameOrEmail(any(), any());

            inOrder.verify(repository, verification.apply(shouldSaveUserToRepo))
                    .save(any());

            inOrder.verify(userMapper, verification.apply(shouldCallMapper))
                    .mapToUserResponse(any());
        }
    }
}