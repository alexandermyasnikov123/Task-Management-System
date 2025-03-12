package ru.alexander.projects.tasks.data.exceptions;

import ru.alexander.projects.shared.data.exceptions.LocalizedRuntimeException;

public class TaskException extends LocalizedRuntimeException {

    public TaskException(String messageCode) {
        super("errors.task.cause", messageCode);
    }
}
