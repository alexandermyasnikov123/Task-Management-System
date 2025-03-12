package ru.alexander.projects.shared.domain.models.responses;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        long totalCount,
        long page,
        long perPage,
        List<T> content
) {

    public static <E> PageResponse<E> fromPage(Page<E> page) {
        final var pageable = page.getPageable();
        return new PageResponse<>(
                page.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getContent()
        );
    }
}
