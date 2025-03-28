package ru.alexander.projects.auths.infrastructure.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.alexander.projects.shared.infrastrusture.CustomMessageSource;

import java.util.Map;

@Lazy
@Component
public class AuthenticationAdapter extends EntryPointAdapter implements AuthenticationEntryPoint {

    public AuthenticationAdapter(ObjectMapper objectMapper, CustomMessageSource messageSource) {
        super(objectMapper, HttpStatus.UNAUTHORIZED, Map.of(
                messageSource.getMessage("errors.authentication.cause"),
                messageSource.getMessage("errors.authentication.details")
        ));
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        super.handle(request, response, authException);
    }
}
