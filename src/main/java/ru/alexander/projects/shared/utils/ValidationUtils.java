package ru.alexander.projects.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.method.ParameterValidationResult;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtils {

    public static List<String> getMessages(ParameterValidationResult validationResult) {
        return validationResult.getResolvableErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();
    }

    public static String getParameter(ParameterValidationResult validationResult) {
        return validationResult.getMethodParameter().getParameterName();
    }
}
