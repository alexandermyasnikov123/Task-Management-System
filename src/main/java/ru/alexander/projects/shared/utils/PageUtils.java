package ru.alexander.projects.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageUtils {

    public static Pageable ofPositive(int page, int perPage) {
        return PageRequest.of(page - 1, perPage);
    }
}
