package ru.alexander.projects.auths.data.services.impls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.alexander.projects.auths.infrastructure.configurations.JwtClaims;

import java.util.concurrent.TimeUnit;

class JwtTokenServiceImplTest {
    private UserDetailsManager userDetailsManager;

    private JwtTokenServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        final var expectedClaims = new JwtClaims()
                .setIssuer("test_issuer")
                .setSecret("secret-phrase")
                .setExpirationMillis(TimeUnit.SECONDS.toMillis(5));

        expectedClaims.initialize();

        userDetailsManager = Mockito.mock();

        jwtService = new JwtTokenServiceImpl(userDetailsManager, expectedClaims);
    }

    @Test
    @DisplayName(value = "A token is creating with same values as in properties if user exists")
    void testValidTokenCreation() {
        final var username = "user1";
        final var role = "role_admin";

        final var expectedUser = User
                .withUsername(username)
                .authorities(role)
                .password("some-password")
                .passwordEncoder(String::toString)
                .build();

        Mockito.when(userDetailsManager.loadUserByUsername(username)).thenReturn(expectedUser);

        jwtService.createToken(username, role);

        Mockito.verify(userDetailsManager).loadUserByUsername(username);
    }

    @Test
    @DisplayName(value = "createToken throws IllegalArgumentException if a user with the actual claims does not exist")
    void testTokenCreationWithNonExistingUser() {
        final var username = "user1";
        final var role = "role_admin";
        testInvalidToken(username, role, () -> {
            Mockito.when(userDetailsManager.loadUserByUsername(username)).thenThrow(UsernameNotFoundException.class);
        });
    }

    @Test
    @DisplayName(value = "createToken throws IllegalArgumentException if a user has different role")
    void testTokenCreationInvalidRole() {
        final var username = "user2";
        final var role = "role_guest";

        final var expectedUser = User
                .withUsername(username)
                .authorities("different-role")
                .password("some-password")
                .passwordEncoder(String::toString)
                .build();

        testInvalidToken(username, role, () -> {
            Mockito.when(userDetailsManager.loadUserByUsername(username)).thenReturn(expectedUser);
        });
    }

    @Test
    @DisplayName(value = "if the token is valid than isTokenValid returns true")
    void testValidTokenIsValid() {
        final var username = "new_user";
        final var role = "role_manager";

        final var expectedUser = User
                .withUsername(username)
                .authorities(role)
                .password("some-password")
                .passwordEncoder(String::toString)
                .build();

        Mockito.when(userDetailsManager.loadUserByUsername(username)).thenReturn(expectedUser);
        final var jwtToken = jwtService.createToken(username, role);

        Assertions.assertTrue(jwtService.isTokenValid(jwtToken));
        Mockito.verify(userDetailsManager, Mockito.times(2)).loadUserByUsername(username);
    }

    @Test
    @DisplayName(value = "if the token is not valid than isTokenValid returns false")
    void testInvalidTokenIsValid() {
        final var username = "new_user";
        final var role = "role_manager";

        final var expectedUser = User
                .withUsername(username)
                .authorities(role)
                .password("some-password")
                .passwordEncoder(String::toString)
                .build();

        Mockito.when(userDetailsManager.loadUserByUsername(username)).thenReturn(expectedUser);
        final var jwtToken = jwtService.createToken(username, role);

        Mockito.when(userDetailsManager.loadUserByUsername(username)).thenThrow(UsernameNotFoundException.class);

        Assertions.assertFalse(jwtService.isTokenValid(jwtToken));
        Mockito.verify(userDetailsManager, Mockito.times(2)).loadUserByUsername(username);
    }

    private void testInvalidToken(String username, String role, Runnable beforeTokenCreation) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            beforeTokenCreation.run();
            jwtService.createToken(username, role);
        }, JwtTokenServiceImpl.CREATION_EXCEPTION_MESSAGE);

        Mockito.verify(userDetailsManager).loadUserByUsername(username);
    }
}