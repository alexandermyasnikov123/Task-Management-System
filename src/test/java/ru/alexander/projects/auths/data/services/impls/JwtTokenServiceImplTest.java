package ru.alexander.projects.auths.data.services.impls;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.alexander.projects.auths.infrastructure.configurations.JwtClaims;
import ru.alexander.projects.auths.utils.TriConsumer;

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
        withUserDetailsContext("username_john", "role_admin", (username, role, userDetails) -> {
            Mockito.when(userDetailsManager.loadUserByUsername(username)).thenReturn(userDetails);
            jwtService.createToken(username, role);
            Mockito.verify(userDetailsManager).loadUserByUsername(username);
        });
    }

    @Test
    @DisplayName(value = "createToken throws IllegalArgumentException if a user with the actual claims does not exist")
    void testTokenCreationWithNonExistingUser() {
        withUserDetailsContext("username_dan", "role_admin", (username, role, userDetails) -> {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Mockito.when(userDetailsManager.loadUserByUsername(username)).thenThrow(UsernameNotFoundException.class);
                jwtService.createToken(username, role);
            }, JwtTokenServiceImpl.CREATION_EXCEPTION_MESSAGE);

            Mockito.verify(userDetailsManager).loadUserByUsername(username);
        });
    }

    @Test
    @DisplayName(value = "createToken throws IllegalArgumentException if a user has different role")
    void testTokenCreationInvalidRole() {
        withUserDetailsContext("username_josh", "role_guest", (username, role, userDetails) -> {
            final var differentUserDetails = User
                    .withUserDetails(userDetails)
                    .authorities("role_different")
                    .build();

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Mockito.when(userDetailsManager.loadUserByUsername(username)).thenReturn(differentUserDetails);
                jwtService.createToken(username, role);
            }, JwtTokenServiceImpl.CREATION_EXCEPTION_MESSAGE);

            Mockito.verify(userDetailsManager).loadUserByUsername(username);
        });
    }

    @Test
    @DisplayName(value = "if the token is valid than isTokenValid returns true")
    void testValidTokenIsValid() {
        withUserDetailsContext("username_dave", "role_manager", (username, role, userDetails) -> {
            Mockito.when(userDetailsManager.loadUserByUsername(username)).thenReturn(userDetails);

            final var jwtToken = jwtService.createToken(username, role);
            Assertions.assertTrue(jwtService.isTokenValid(jwtToken));

            Mockito.verify(userDetailsManager, Mockito.times(2)).loadUserByUsername(username);
        });
    }

    @Test
    @DisplayName(value = "if the token is not valid than isTokenValid returns false")
    void testInvalidTokenIsValid() {
        withUserDetailsContext("usename_kyle", "role_trainee", (username, role, userDetails) -> {
            Mockito.when(userDetailsManager.loadUserByUsername(username)).thenReturn(userDetails);

            final var jwtToken = jwtService.createToken(username, role);
            Mockito.when(userDetailsManager.loadUserByUsername(username)).thenThrow(UsernameNotFoundException.class);

            Assertions.assertFalse(jwtService.isTokenValid(jwtToken));
            Mockito.verify(userDetailsManager, Mockito.times(2)).loadUserByUsername(username);
        });
    }

    /**
     * Removes unnecessary code duplication between different test cases
     * The context is nothing just a provider with a username, a role and details.
     * Kotlin DSL-like style.
     * */
    private void withUserDetailsContext(
            String username,
            String role,
            TriConsumer<String, String, UserDetails> context
    ) {
        final var expectedUser = User
                .withUsername(username)
                .authorities(role)
                .password("some-password")
                .passwordEncoder(String::toString)
                .build();

        context.accept(username, role, expectedUser);
    }
}