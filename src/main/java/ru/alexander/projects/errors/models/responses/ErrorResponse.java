package ru.alexander.projects.errors.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

public record ErrorResponse(
        @Schema(description = "errors.issuedAt.desc")
        LocalDateTime issuedAt,
        @Schema(description = "errors.details.desc")
        MultiValueMap<String, String> details
) {
    public static ErrorResponse fromDetails(MultiValueMap<String, String> details) {
        return new ErrorResponse(LocalDateTime.now(), details);
    }
}
