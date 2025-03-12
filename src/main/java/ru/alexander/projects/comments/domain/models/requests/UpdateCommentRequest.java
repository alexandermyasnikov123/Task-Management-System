package ru.alexander.projects.comments.domain.models.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
        @NotBlank
        String comment
) {
}
