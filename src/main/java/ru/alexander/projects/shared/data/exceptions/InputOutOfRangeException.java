package ru.alexander.projects.shared.data.exceptions;

import java.util.List;

public class InputOutOfRangeException extends LocalizedRuntimeException{

    public InputOutOfRangeException(List<? extends CharSequence> availableValues) {
        super("errors.invalid-input.cause", "errors.invalid-input.details", availableValues);
    }
}
