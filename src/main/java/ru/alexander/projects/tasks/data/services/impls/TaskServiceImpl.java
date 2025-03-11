package ru.alexander.projects.tasks.data.services.impls;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.alexander.projects.shared.domain.models.responses.PageResponse;
import ru.alexander.projects.tasks.data.mapper.TaskMapper;
import ru.alexander.projects.tasks.data.repositories.TaskRepository;
import ru.alexander.projects.tasks.domain.models.requests.AddCommentRequest;
import ru.alexander.projects.tasks.domain.models.requests.CreateTaskRequest;
import ru.alexander.projects.tasks.domain.models.requests.UpdateTaskRequest;
import ru.alexander.projects.tasks.domain.models.responses.TaskResponse;
import ru.alexander.projects.tasks.domain.services.TaskService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskServiceImpl implements TaskService {
    TaskRepository repository;

    TaskMapper taskMapper;

    @Override
    public PageResponse<TaskResponse> findAllTasks(int page, int perPage) {
        final var pageResponse = repository.findAll(PageRequest.of(page - 1, perPage));

        return new PageResponse<>(
                pageResponse.getTotalElements(),
                page,
                perPage,
                pageResponse.map(taskMapper::mapToResponse).toList()
        );
    }

    @Override
    public PageResponse<TaskResponse> findTasksByQueryFilter(int page, int perPage, String query) {
        return null;
    }

    @Override
    public TaskResponse createNewTask(CreateTaskRequest request) {
        return null;
    }

    @Override
    public TaskResponse updateExistingTask(UpdateTaskRequest request) {
        return null;
    }

    @Override
    public void addCommentsToTask(AddCommentRequest request) {

    }

    @Override
    public void updateTaskStatus(long taskId, String newStatus) {

    }

    @Override
    public void updateTaskPriority(long taskId, String newPriority) {

    }

    @Override
    public void deleteTasksByIds(long id, long... taskIds) {

    }
}
