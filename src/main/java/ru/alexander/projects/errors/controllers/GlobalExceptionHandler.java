package ru.alexander.projects.errors.controllers;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.alexander.projects.auths.data.exceptions.InvalidUserPasswordException;
import ru.alexander.projects.errors.models.responses.ErrorResponse;
import ru.alexander.projects.shared.data.exceptions.LocalizedRuntimeException;
import ru.alexander.projects.shared.infrastrusture.CustomMessageSource;
import ru.alexander.projects.shared.utils.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final CustomMessageSource messageSource;

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        return buildErrorResponse(e.getConstraintViolations()
                .stream()
                .map(violation -> Pair.of(violation.getPropertyPath().toString(), violation.getMessage()))
        );
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<?> handleBindExceptions(BindException e) {
        return buildErrorResponse(e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> Pair.of(error.getField(), StringUtils.defaultString(error.getDefaultMessage())))
        );
    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<?> handleHandlerExceptions(HandlerMethodValidationException e) {
        final var details = e.getParameterValidationResults()
                .stream()
                .collect(Collectors.toMap(ValidationUtils::getParameter, ValidationUtils::getMessages));

        return buildErrorResponse(details);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleRequestParamsException(MissingServletRequestParameterException e) {
        final var details = Pair.of(e.getParameterName(), e.getLocalizedMessage());
        return buildErrorResponse(Stream.of(details));
    }

    @ExceptionHandler(value = LocalizedRuntimeException.class)
    public ResponseEntity<?> handleLocalizedRuntimeException(LocalizedRuntimeException e) {
        final var details = e.toMessagePair(messageSource);
        return buildErrorResponse(Stream.of(details));
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException() {
        return handleLocalizedRuntimeException(new InvalidUserPasswordException());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Stream<? extends Map.Entry<String, String>> mappings
    ) {
        final var details = mappings.collect(Collectors.groupingBy(
                Map.Entry::getKey,
                HashMap::new,
                Collectors.mapping(Map.Entry::getValue, Collectors.toList()))
        );

        return buildErrorResponse(details);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Map<String, List<String>> details) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.fromDetails(MultiValueMap.fromMultiValue(details)));
    }
}
