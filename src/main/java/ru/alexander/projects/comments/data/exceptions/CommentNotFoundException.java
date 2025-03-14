package ru.alexander.projects.comments.data.exceptions;

import java.util.List;

public class CommentNotFoundException extends CommentException {

    public CommentNotFoundException(Long commentId) {
        super("errors.comment-not-found.details", List.of(commentId));
    }
}
