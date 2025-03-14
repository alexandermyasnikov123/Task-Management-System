package ru.alexander.projects.comments.domain.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @Schema(description = "comment.message.desc")
        @NotBlank
        String comment
) {
}
