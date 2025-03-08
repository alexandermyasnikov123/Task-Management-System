package ru.alexander.projects.shared.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUtils {

    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest);
    }
}
