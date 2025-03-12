package ru.alexander.projects.tasks.data.exceptions;

public class TaskNotFoundException extends TaskException {

    public TaskNotFoundException() {
        super("errors.task-not-found.details");
    }
}
