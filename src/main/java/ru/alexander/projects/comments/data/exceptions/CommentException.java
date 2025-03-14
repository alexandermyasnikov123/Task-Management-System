package ru.alexander.projects.comments.data.exceptions;

import ru.alexander.projects.shared.data.exceptions.LocalizedRuntimeException;

import java.util.List;

public abstract class CommentException extends LocalizedRuntimeException {

    protected CommentException(String messageCode, List<?> messageArguments) {
        super("errors.comment.cause", messageCode, messageArguments);
    }
}
