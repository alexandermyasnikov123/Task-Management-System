package ru.alexander.projects.tasks.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.alexander.projects.tasks.domain.models.requests.CreateTaskRequest;
import ru.alexander.projects.tasks.domain.models.requests.UpdateTaskRequest;
import ru.alexander.projects.tasks.domain.services.TaskService;

import java.net.URI;

@RestController
@RequestMapping(value = "/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;

    @PostMapping
    public ResponseEntity<?> createTask(
            @Valid
            @RequestBody
            CreateTaskRequest request
    ) {
        final var response = service.createNewTask(request);
        return ResponseEntity
                .created(URI.create("/" + response.id()))
                .body(response);
    }

    @PutMapping(value = "/{TASK_ID}")
    @PreAuthorize(value = "hasRole('ADMIN') or @taskServiceImpl.isTaskOwner(#taskId)")
    public ResponseEntity<?> updateTask(
            @NotNull
            @Positive
            @PathVariable(value = "TASK_ID")
            Long taskId,
            @Valid
            @RequestBody
            UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(service.updateTask(taskId, request));
    }

    @PatchMapping(value = "/{TASK_ID}/status")
    @PreAuthorize(value = """
            hasRole('ADMIN') \
            or @taskServiceImpl.isTaskOwner(#taskId) \
            or @taskServiceImpl.isTaskContractor(#taskId)""")
    public ResponseEntity<?> updateStatus(
            @NotNull
            @Positive
            @PathVariable(value = "TASK_ID")
            Long taskId,
            @NotBlank
            String status
    ) {
        service.updateTaskStatus(taskId, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{TASK_ID}/priority")
    @PreAuthorize(value = """
            hasRole('ADMIN') \
            or @taskServiceImpl.isTaskOwner(#taskId) \
            or @taskServiceImpl.isTaskContractor(#taskId)""")
    public ResponseEntity<?> updatePriority(
            @NotNull
            @Positive
            @PathVariable(value = "TASK_ID")
            Long taskId,
            @NotBlank
            String priority
    ) {
        service.updateTaskPriority(taskId, priority);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{TASK_ID}")
    @PreAuthorize(value = "hasRole('ADMIN') or @taskServiceImpl.isTaskOwner(#taskId)")
    public ResponseEntity<?> deleteTask(
            @NotNull
            @Positive
            @PathVariable(value = "TASK_ID")
            Long taskId
    ) {
        service.deleteTaskById(taskId);
        return ResponseEntity.ok().build();
    }
}
