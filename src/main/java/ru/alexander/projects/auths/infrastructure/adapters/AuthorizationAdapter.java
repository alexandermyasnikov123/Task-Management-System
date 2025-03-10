package ru.alexander.projects.auths.infrastructure.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import ru.alexander.projects.shared.infrastrusture.CustomMessageSource;

import java.util.Map;

@Lazy
@Component
public class AuthorizationAdapter extends EntryPointAdapter implements AccessDeniedHandler {

    public AuthorizationAdapter(ObjectMapper objectMapper, CustomMessageSource messageSource) {
        super(objectMapper, HttpStatus.FORBIDDEN, Map.of(
                messageSource.getMessage("errors.access-denied.cause"),
                messageSource.getMessage("errors.access-denied.details")
        ));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        super.handle(request, response, accessDeniedException);
    }
}
