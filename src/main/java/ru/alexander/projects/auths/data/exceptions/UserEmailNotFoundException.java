package ru.alexander.projects.auths.data.exceptions;

public class UserEmailNotFoundException extends UserException {

    public UserEmailNotFoundException() {
        super("errors.cant-find-user.email.details");
    }
}
