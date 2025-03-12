package ru.alexander.projects.tasks.domain.models.requests;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record UpdateTaskRequest(
        @NotBlank
        String title,
        @NotBlank
        String details,
        @NotBlank
        @UUID
        String contractorId,
        @NotBlank
        String priority,
        @NotBlank
        String status
) {
}
