package ru.alexander.projects.shared.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServletUtils {

    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest);
    }

    public static Cookie buildBasePathCookie(String name, String value) {
        final var cookie = new Cookie(name, value);
        final var basePath = getCurrentRequest().map(HttpServletRequest::getContextPath).orElseThrow();

        cookie.setPath(basePath);

        return cookie;
    }
}
