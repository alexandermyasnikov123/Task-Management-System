package ru.alexander.projects.auths.domain.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

@Value
@NonFinal
@SuperBuilder
public class AuthUserRequest {
    @Schema(description = "user.email.desc", example = "user123@gmail.com")
    @Email
    String email;

    @Schema(description = "user.password.desc", example = "username_123")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,100}")
    String password;
}
