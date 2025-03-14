package ru.alexander.projects.comments.domain.services;

import ru.alexander.projects.comments.domain.models.requests.CreateCommentRequest;
import ru.alexander.projects.comments.domain.models.requests.UpdateCommentRequest;
import ru.alexander.projects.comments.domain.models.responses.CommentResponse;
import ru.alexander.projects.shared.domain.models.responses.PageResponse;

public interface CommentService {
    boolean canModifyComment(Long commentId);

    boolean canAddComments(Long taskId);

    PageResponse<CommentResponse> findAll(Long taskId, Integer page, Integer perPage);

    CommentResponse findById(Long commentId);

    CommentResponse createComment(Long taskId, CreateCommentRequest request);

    CommentResponse updateComment(Long commentId, UpdateCommentRequest request);

    void deleteComment(Long commentId);
}
