package ru.alexander.projects.tasks.domain.models.responses;

import ru.alexander.projects.auths.domain.models.responses.UserResponse;
import ru.alexander.projects.comments.domain.models.responses.CommentResponse;

import java.util.List;

public record TaskResponse(
        Long id,
        String title,
        String details,
        UserResponse owner,
        UserResponse contractor,
        List<CommentResponse> comments
) {
}
