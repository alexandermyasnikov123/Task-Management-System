package ru.alexander.projects.auths.domain.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthRequest(
        @Schema(description = "user.email.desc", example = "example@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "user.password.desc", example = "&Password1234&")
        @NotBlank
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,100}")
        String password
) {
}
