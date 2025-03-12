package ru.alexander.projects.comments.data.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexander.projects.comments.data.entities.CommentEntity;
import ru.alexander.projects.comments.data.exceptions.CommentNotFoundException;
import ru.alexander.projects.comments.data.mappers.CommentMapper;
import ru.alexander.projects.comments.data.repositories.CommentRepository;
import ru.alexander.projects.comments.domain.models.requests.CreateCommentRequest;
import ru.alexander.projects.comments.domain.models.requests.UpdateCommentRequest;
import ru.alexander.projects.comments.domain.models.responses.CommentResponse;
import ru.alexander.projects.comments.domain.services.CommentService;
import ru.alexander.projects.shared.domain.models.responses.PageResponse;
import ru.alexander.projects.shared.utils.UserUtils;
import ru.alexander.projects.tasks.data.repositories.TaskRepository;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;

    TaskRepository taskRepository;

    CommentMapper mapper;

    @Override
    public boolean canModifyComment(Long commentId) {
        return UserUtils.getPrincipalId().flatMap(principalId -> commentRepository.findById(commentId)
                .map(comment -> comment.getOwner().getId())
                .map(principalId::equals)
        ).orElse(false);
    }

    @Override
    public boolean canAddComments(Long taskId) {
        return UserUtils.getPrincipalId().flatMap(principalId -> taskRepository.findById(taskId).map(task -> {
            final var isOwner = task.getOwner().getId().equals(principalId);
            final var isContractor = task.getContractor().getId().equals(principalId);
            return isOwner || isContractor;
        })).orElse(false);
    }

    @Override
    public CommentResponse findById(Long commentId) {
        final var entity = requireCommentById(commentId, Function.identity());

        return mapper.mapToResponse(entity);
    }

    @Override
    public PageResponse<CommentResponse> findAll(Long taskId, Integer page, Integer perPage) {
        return PageResponse.fromPage(commentRepository
                .findAllByTaskId(taskId, PageRequest.of(page - 1, perPage))
                .map(mapper::mapToResponse)
        );
    }

    @Override
    public CommentResponse createComment(Long taskId, CreateCommentRequest request) {
        final var entity = mapper.mapToEntity(request);

        commentRepository.saveAndFlush(entity);
        return mapper.mapToResponse(entity);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request) {
        final var entity = requireCommentById(commentId, comment -> comment.setComment(request.comment()));

        return mapper.mapToResponse(entity);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private CommentEntity requireCommentById(Long commentId, Function<CommentEntity, CommentEntity> mapper) {
        return commentRepository.findById(commentId)
                .map(mapper)
                .orElseThrow(CommentNotFoundException::new);
    }
}
