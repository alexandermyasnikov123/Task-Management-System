package ru.alexander.projects.comments.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alexander.projects.comments.domain.models.requests.CreateCommentRequest;
import ru.alexander.projects.comments.domain.models.requests.UpdateCommentRequest;
import ru.alexander.projects.comments.domain.services.CommentService;

import java.net.URI;

@RestController
@RequestMapping(value = "/tasks/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.canAddComments(#taskId)")
    public ResponseEntity<?> createComment(
            @NotNull
            @Positive
            @RequestParam
            Long taskId,
            @Valid
            @RequestBody
            CreateCommentRequest request
    ) {
        final var response = service.createComment(taskId, request);
        return ResponseEntity
                .created(URI.create("/" + response.id()))
                .body(response);
    }

    @GetMapping(value = "/{COMMENT_ID}")
    public ResponseEntity<?> findComment(
            @NotNull
            @Positive
            @PathVariable(value = "COMMENT_ID")
            Long commentId
    ) {
        return ResponseEntity.ok(service.findById(commentId));
    }

    @GetMapping
    public ResponseEntity<?> findComments(
            @NotNull
            @Positive
            @RequestParam
            Long taskId,
            @NotNull
            @Positive
            Integer page,
            @NotNull
            @Positive
            Integer perPage
    ) {
        return ResponseEntity.ok(service.findAll(taskId, page, perPage));
    }

    @PatchMapping(value = "/{COMMENT_ID}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.canModifyComment(#commentId)")
    public ResponseEntity<?> updateComment(
            @NotNull
            @Positive
            @PathVariable(value = "COMMENT_ID")
            Long commentId,
            @Valid
            @RequestBody
            UpdateCommentRequest request
    ) {
        return ResponseEntity.ok(service.updateComment(commentId, request));
    }

    @DeleteMapping(value = "/{COMMENT_ID}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.canModifyComment(#commentId)")
    public ResponseEntity<?> deleteComment(
            @NotNull
            @Positive
            @PathVariable(value = "COMMENT_ID")
            Long commentId
    ) {
        service.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
