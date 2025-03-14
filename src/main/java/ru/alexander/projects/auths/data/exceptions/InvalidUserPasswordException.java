package ru.alexander.projects.auths.data.exceptions;

public class InvalidUserPasswordException extends UserException {

    public InvalidUserPasswordException() {
        super("errors.invalid-password.details");
    }
}
