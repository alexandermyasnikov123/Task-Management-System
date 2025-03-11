package ru.alexander.projects.tasks.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.alexander.projects.shared.utils.EnumUtils;
import ru.alexander.projects.tasks.data.entities.TaskEntity;
import ru.alexander.projects.tasks.data.entities.TaskPriority;
import ru.alexander.projects.tasks.data.entities.TaskStatus;
import ru.alexander.projects.tasks.domain.models.requests.CreateTaskRequest;
import ru.alexander.projects.tasks.domain.models.responses.TaskResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "contractor", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "priority", expression = "java(mapToPriority(request))")
    @Mapping(target = "status", expression = "java(mapToStatus(request))")
    TaskEntity mapToEntity(CreateTaskRequest request);

    TaskResponse mapToResponse(TaskEntity entity);

    default TaskPriority mapToPriority(CreateTaskRequest request) {
        return EnumUtils.uppercaseValueOf(TaskPriority.class, request.priority());
    }

    default TaskStatus mapToStatus(CreateTaskRequest request) {
        return EnumUtils.uppercaseValueOf(TaskStatus.class, request.status());
    }
}
