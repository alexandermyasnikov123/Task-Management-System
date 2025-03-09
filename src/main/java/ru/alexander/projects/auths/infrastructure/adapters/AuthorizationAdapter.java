package ru.alexander.projects.auths.infrastructure.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Lazy
@Component
public class AuthorizationAdapter extends EntryPointAdapter implements AccessDeniedHandler {

    public AuthorizationAdapter(ObjectMapper objectMapper, MessageSource messageSource) {
        super(objectMapper, HttpStatus.FORBIDDEN, Map.of(
                messageSource.getMessage("errors.access-denied.cause", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("errors.access-denied.details", null, LocaleContextHolder.getLocale())
        ));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        super.handle(request, response, accessDeniedException);
    }
}
