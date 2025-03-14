package ru.alexander.projects.tasks.data.services.impls;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexander.projects.auths.data.entities.UserEntity;
import ru.alexander.projects.shared.domain.models.responses.PageResponse;
import ru.alexander.projects.shared.utils.EnumUtils;
import ru.alexander.projects.shared.utils.PageUtils;
import ru.alexander.projects.shared.utils.UserUtils;
import ru.alexander.projects.tasks.data.entities.TaskEntity;
import ru.alexander.projects.tasks.data.entities.TaskPriority;
import ru.alexander.projects.tasks.data.entities.TaskStatus;
import ru.alexander.projects.tasks.data.exceptions.TaskNotFoundException;
import ru.alexander.projects.tasks.data.mapper.TaskMapper;
import ru.alexander.projects.tasks.data.repositories.TaskRepository;
import ru.alexander.projects.tasks.domain.models.requests.CreateTaskRequest;
import ru.alexander.projects.tasks.domain.models.requests.UpdateTaskRequest;
import ru.alexander.projects.tasks.domain.models.responses.TaskResponse;
import ru.alexander.projects.tasks.domain.services.TaskService;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskServiceImpl implements TaskService {
    TaskRepository repository;

    TaskMapper mapper;

    @Override
    public boolean isTaskOwner(Long taskId) {
        return checkCurrentUserIs(taskId, TaskEntity::getOwner);
    }

    @Override
    public boolean isTaskContractor(Long taskId) {
        return checkCurrentUserIs(taskId, TaskEntity::getContractor);
    }

    @Override
    public PageResponse<TaskResponse> findAllTasks(Integer page, Integer perPage, String queryFilter) {
        final var pageable = PageUtils.ofPositive(page, perPage);
        final var sqlFilter = StringUtils.isBlank(queryFilter) ? null : "%" + queryFilter + "%";

        final var pageResponse = repository
                .findFilteredTasks(sqlFilter, pageable)
                .map(mapper::mapToResponse);

        return PageResponse.fromPage(pageResponse);
    }

    @Override
    public TaskResponse createNewTask(CreateTaskRequest request) {
        final var entity = mapper.mapToEntity(request);
        repository.save(entity);

        return mapper.mapToResponse(entity);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        final var data = transformAndFlush(taskId, task -> {
            final var entity = mapper.mapToEntity(task.getId(), request, task.getOwner().getId());
            return repository.save(entity);
        });

        return mapper.mapToResponse(data);
    }

    @Override
    @Transactional
    public void updateTaskStatus(Long taskId, String newStatus) {
        final var enumStatus = EnumUtils.uppercaseValueOf(TaskStatus.class, newStatus);
        transformAndFlush(taskId, task -> task.setStatus(enumStatus));
    }

    @Override
    @Transactional
    public void updateTaskPriority(Long taskId, String newPriority) {
        final var enumPriority = EnumUtils.uppercaseValueOf(TaskPriority.class, newPriority);
        transformAndFlush(taskId, task -> task.setPriority(enumPriority));
    }

    @Override
    @Transactional
    public void deleteTaskById(Long taskId) {
        repository.deleteById(taskId);
    }

    private TaskEntity transformAndFlush(Long taskId, Function<TaskEntity, TaskEntity> mapper) {
        return repository.findById(taskId)
                .map(mapper)
                .map(repository::saveAndFlush)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    private boolean checkCurrentUserIs(Long taskId, Function<TaskEntity, UserEntity> userProvider) {
        return UserUtils.getPrincipalId().flatMap(userId -> repository.findById(taskId)
                .map(userProvider)
                .map(UserEntity::getId)
                .map(userId::equals)
        ).orElse(false);
    }
}
