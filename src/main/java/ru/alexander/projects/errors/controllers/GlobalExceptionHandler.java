package ru.alexander.projects.errors.controllers;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.alexander.projects.errors.models.responses.ErrorResponse;
import ru.alexander.projects.shared.data.exceptions.LocalizedRuntimeException;
import ru.alexander.projects.shared.infrastrusture.CustomMessageSource;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final CustomMessageSource messageSource;

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getConstraintViolations()
                .stream()
                .map(violation -> Pair.of(violation.getPropertyPath().toString(), violation.getMessage()))
        );
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<?> handleBindExceptions(BindException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> Pair.of(error.getField(), StringUtils.defaultString(error.getDefaultMessage())))
        );
    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<?> handleHandlerExceptions(HandlerMethodValidationException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getAllErrors()
                .stream()
                .map(source -> Pair.of(e.getMethod().getName(), StringUtils.defaultString(source.getDefaultMessage())))
        );
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleRequestParamsException(MissingServletRequestParameterException e) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                Stream.of(Pair.of(e.getParameterName(), e.getLocalizedMessage()))
        );
    }

    @ExceptionHandler(value = LocalizedRuntimeException.class)
    public ResponseEntity<?> handleLocalizedRuntimeException(LocalizedRuntimeException e) {
        final var details = e.toMessagePair(messageSource);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, Stream.of(details));
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException() {
        return handleLocalizedRuntimeException(
                new LocalizedRuntimeException("errors.authentication.cause", "errors.authentication.password")
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatusCode statusCode,
            Stream<Pair<String, String>> mappings
    ) {
        final var details = mappings.collect(Collectors.groupingBy(
                Pair::getFirst,
                HashMap::new,
                Collectors.mapping(Pair::getSecond, Collectors.toList())
        ));

        return ResponseEntity
                .status(statusCode)
                .body(ErrorResponse.fromDetails(MultiValueMap.fromMultiValue(details)));
    }
}
