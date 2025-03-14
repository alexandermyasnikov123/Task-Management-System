package ru.alexander.projects.integration.auths;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import ru.alexander.projects.auths.domain.models.requests.AuthRequest;
import ru.alexander.projects.auths.domain.models.requests.RegisterRequest;
import ru.alexander.projects.auths.domain.models.responses.AuthResponse;
import ru.alexander.projects.auths.domain.services.JwtTokenService;
import ru.alexander.projects.integration.tests.BaseIntegrationTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.alexander.projects.integration.utils.IntegrationTestUtils.givenApiRequest;
import static ru.alexander.projects.integration.utils.IntegrationTestUtils.validateApiResponse;

public class AuthsTest extends BaseIntegrationTest {

    @Test
    @DisplayName(value = "endpoint returns 200 when data is correct and user is unique")
    void testCorrectRegister() {
        final var userData = registerCorrectUser();
        final var request = userData.getFirst();
        final var response = userData.getSecond();

        final var jwtToken = response.getCookie(JwtTokenService.TOKEN_COOKIE);
        final var authResponse = response.as(AuthResponse.class);

        assertThat(authResponse.jwtToken(), equalTo(jwtToken));
        assertThat(authResponse.username(), equalTo(request.username()));
        assertThat(authResponse.role(), equalTo(request.role()));
    }

    @Test
    @DisplayName(value = "endpoint returns 400 when data is correct but user is not unique")
    void testCantRegisterTwice() {
        final var responses = IntStream.range(0, 2).mapToObj(ignored -> {
            final var userData = registerCorrectUser();
            return userData.getSecond();
        }).toList();

        validateApiResponse(responses.getFirst())
                .statusCode(HttpStatus.OK.value())
                .cookie(JwtTokenService.TOKEN_COOKIE, not(blankString()));

        validateApiResponse(responses.getLast())
                .statusCode(HttpStatus.BAD_REQUEST.value());

        assertThat(responses.getLast().getCookie(JwtTokenService.TOKEN_COOKIE), nullValue());
    }

    @Test
    @DisplayName(value = "endpoint returns 400 when data is invalid")
    void testCantRegisterInvalidRequestBody() {
        final var invalidRequests = List.of(
                Pair.of("username", new RegisterRequest("invalid username", "email@valid.com", "ADMIN", "1normalPASSWORD&&2")),
                Pair.of("email", new RegisterRequest("valid_username", "invalid_email", "ADMIN", "1normalPASSWORD&&2")),
                Pair.of("role", new RegisterRequest("valid_username", "email@valid.com", "invalid role", "1normalPASSWORD&&2")),
                Pair.of("password", new RegisterRequest("valid_username", "email@valid.com", "ADMIN", "invalid password"))
        );

        invalidRequests.forEach(pair -> {
            final var response = givenApiRequest(pair.getSecond())
                    .post("/auth/register");

            validateApiResponse(response)
                    .statusCode(HttpStatus.BAD_REQUEST.value());

            final var details = response.jsonPath().getMap("details", String.class, List.class);
            assertTrue(details.containsKey(pair.getFirst()));
        });
    }

    @Test
    @DisplayName(value = "can't auth if there is no user with actual credentials")
    void testCantLoginNonExistingUser() {
        final var requestBody = new AuthRequest("valid@email.com", "v4l1d&P4S&");
        final var response = givenApiRequest(requestBody)
                .post("/auth/login");

        validateApiResponse(response)
                .statusCode(HttpStatus.BAD_REQUEST.value());

        assertThat(response.getCookie(JwtTokenService.TOKEN_COOKIE), nullValue());
    }

    @Test
    @DisplayName(value = "user success auth if its account exists and credentials are valid")
    void testCorrectLogin() {
        final var userData = registerCorrectUser();
        final var request = userData.getFirst();

        final var email = request.email();
        final var password = request.password();

        final var authRequest = new AuthRequest(email, password);
        final var response = givenApiRequest(authRequest)
                .post("/auth/login");

        validateApiResponse(response)
                .statusCode(HttpStatus.OK.value())
                .cookie(JwtTokenService.TOKEN_COOKIE, not(nullValue()));

    }

    private Pair<RegisterRequest, Response> registerCorrectUser() {
        final var request = new RegisterRequest(
                "valid_username",
                "valid@email.com",
                "ADMIN",
                "VALid&&P4ssW0rD"
        );
        return Pair.of(request, givenApiRequest(request).post("/auth/register"));
    }
}
