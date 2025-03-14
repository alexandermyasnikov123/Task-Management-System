package ru.alexander.projects.auths.infrastructure.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import ru.alexander.projects.errors.models.responses.ErrorResponse;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EntryPointAdapter {
    ObjectMapper objectMapper;

    HttpStatus httpStatus;

    Map<String, String> details;

    @SneakyThrows
    public void handle(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.getWriter().write(objectMapper.writeValueAsString(
                ErrorResponse.fromDetails(MultiValueMap.fromSingleValue(details))
        ));
    }
}
