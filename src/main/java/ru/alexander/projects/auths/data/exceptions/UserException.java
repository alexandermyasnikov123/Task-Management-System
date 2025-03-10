package ru.alexander.projects.auths.data.exceptions;

import ru.alexander.projects.shared.data.exceptions.LocalizedRuntimeException;

public abstract class UserException extends LocalizedRuntimeException {

    protected UserException(String messageCode) {
        super("errors.user.cause", messageCode);
    }
}
