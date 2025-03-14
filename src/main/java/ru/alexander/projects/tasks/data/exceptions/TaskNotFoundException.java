package ru.alexander.projects.tasks.data.exceptions;

import java.util.List;

public class TaskNotFoundException extends TaskException {

    public TaskNotFoundException(Long taskId) {
        super("errors.task-not-found.details", List.of(taskId));
    }
}
