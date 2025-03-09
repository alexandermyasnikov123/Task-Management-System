package ru.alexander.projects.auths.domain.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class RegisterRequest extends AuthRequest {
    @Schema(description = "user.username.desc", example = "_P4sSw0rD_")
    @Pattern(regexp = "^(?!.*[.\\-_]{2})[a-zA-Z0-9.\\-_]{3,24}$")
    String username;

    @Schema(description = "user.role.desc", example = "admin")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$")
    String role;

    public RegisterRequest(String username, String email, String role, String password) {
        super(email, password);
        this.username = username;
        this.role = role;
    }
}
