package ru.alexander.projects.tasks.domain.services;

import ru.alexander.projects.shared.domain.models.responses.PageResponse;
import ru.alexander.projects.tasks.domain.models.requests.CreateTaskRequest;
import ru.alexander.projects.tasks.domain.models.requests.UpdateTaskRequest;
import ru.alexander.projects.tasks.domain.models.responses.TaskResponse;

public interface TaskService {
    boolean isTaskOwner(Long taskId);

    boolean isTaskContractor(Long taskId);

    PageResponse<TaskResponse> findAllTasks(Integer page, Integer perPage, String queryFilter);

    TaskResponse createNewTask(CreateTaskRequest request);

    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);

    void updateTaskStatus(Long taskId, String newStatus);

    void updateTaskPriority(Long taskId, String newPriority);

    void deleteTaskById(Long taskId);
}
