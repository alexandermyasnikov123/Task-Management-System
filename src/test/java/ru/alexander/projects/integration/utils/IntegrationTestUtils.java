package ru.alexander.projects.integration.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.startsWith;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IntegrationTestUtils {

    public static <T> RequestSpecification givenApiRequest(T requestBody) {
        return given()
                .given()
                .when()
                .request()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    public static ValidatableResponse validateApiResponse(Response response) {
        return response
                .then()
                .contentType(startsWith(ContentType.JSON.toString()));
    }
}
