package ru.alexander.projects.tasks.domain.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record AddCommentRequest(
        @NotNull
        @Positive
        Long taskId,
        @NotEmpty
        List<@NotBlank String> comments
) {
}
