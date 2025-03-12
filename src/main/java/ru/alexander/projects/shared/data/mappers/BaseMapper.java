package ru.alexander.projects.shared.data.mappers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alexander.projects.auths.data.entities.UserEntity;
import ru.alexander.projects.auths.data.repositories.UserRepository;
import ru.alexander.projects.shared.utils.UserUtils;
import ru.alexander.projects.tasks.data.entities.TaskEntity;
import ru.alexander.projects.tasks.data.repositories.TaskRepository;

import java.util.UUID;

@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseMapper {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    protected final UserEntity getUser(UUID userId) {
        return getUserRepository().getReferenceById(userId);
    }

    protected final UserEntity getCurrentUser() {
        return UserUtils.getPrincipalId()
                .map(this::getUser)
                .orElseThrow();
    }

    protected final TaskEntity getTask(Long taskId) {
        return getTaskRepository().getReferenceById(taskId);
    }
}
