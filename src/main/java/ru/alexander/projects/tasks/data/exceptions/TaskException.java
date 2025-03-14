package ru.alexander.projects.tasks.data.exceptions;

import ru.alexander.projects.shared.data.exceptions.LocalizedRuntimeException;

import java.util.List;

public abstract class TaskException extends LocalizedRuntimeException {

    protected TaskException(String messageCode, List<?> messageArguments) {
        super("errors.task.cause", messageCode, messageArguments);
    }
}
