package ru.alexander.projects.auths.data.exceptions;

public class UserNameNotFoundException extends UserException {

    public UserNameNotFoundException() {
        super("errors.cant-find-user.username.details");
    }
}
