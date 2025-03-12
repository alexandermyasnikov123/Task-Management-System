package ru.alexander.projects.comments.data.exceptions;

import ru.alexander.projects.shared.data.exceptions.LocalizedRuntimeException;

public class CommentNotFoundException extends LocalizedRuntimeException {

    public CommentNotFoundException() {
        super("errors.comment.cause", "errors.comment-not-found.details");
    }
}
