package ru.alexander.projects.comments.domain.models.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        Long id,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID ownerId,
        Long taskId
) {
}
