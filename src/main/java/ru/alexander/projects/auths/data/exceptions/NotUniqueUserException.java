package ru.alexander.projects.auths.data.exceptions;

public class NotUniqueUserException extends UserException {

    public NotUniqueUserException() {
        super("errors.not-unique-user.details");
    }
}
