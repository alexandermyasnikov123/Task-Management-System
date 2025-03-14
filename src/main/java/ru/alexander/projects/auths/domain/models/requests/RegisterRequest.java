package ru.alexander.projects.auths.domain.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @Schema(description = "user.email.desc", example = "example@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "user.password.desc", example = "&Password1234&")
        @NotBlank
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,100}")
        String password,

        @Schema(description = "user.username.desc", example = "john_doe")
        @NotBlank
        @Pattern(regexp = "^(?!.*[.\\-_]{2})[a-zA-Z0-9.\\-_]{3,24}$")
        String username,

        @Schema(description = "user.role.desc", example = "user")
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9]{3,}$")
        String role
) {
}
