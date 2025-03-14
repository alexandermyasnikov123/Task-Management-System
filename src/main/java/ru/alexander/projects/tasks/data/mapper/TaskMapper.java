package ru.alexander.projects.tasks.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.alexander.projects.shared.data.mappers.BaseMapper;
import ru.alexander.projects.shared.utils.EnumUtils;
import ru.alexander.projects.tasks.data.entities.TaskEntity;
import ru.alexander.projects.tasks.data.entities.TaskPriority;
import ru.alexander.projects.tasks.data.entities.TaskStatus;
import ru.alexander.projects.tasks.domain.models.requests.CreateTaskRequest;
import ru.alexander.projects.tasks.domain.models.requests.UpdateTaskRequest;
import ru.alexander.projects.tasks.domain.models.responses.TaskResponse;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TaskMapper extends BaseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "status", expression = "java(mapToStatus(request.status()))")
    @Mapping(target = "priority", expression = "java(mapToPriority(request.priority()))")
    @Mapping(target = "owner", expression = "java(getCurrentUser())")
    @Mapping(target = "contractor", expression = "java(getUser(request.contractorId()))")
    public abstract TaskEntity mapToEntity(CreateTaskRequest request);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "owner", expression = "java(getUser(ownerId))")
    @Mapping(target = "contractor", expression = "java(getUser(request.contractorId()))")
    @Mapping(target = "status", expression = "java(mapToStatus(request.status()))")
    @Mapping(target = "priority", expression = "java(mapToPriority(request.priority()))")
    public abstract TaskEntity mapToEntity(Long id, UpdateTaskRequest request, UUID ownerId);

    public abstract TaskResponse mapToResponse(TaskEntity entity);

    protected final TaskPriority mapToPriority(String priority) {
        return EnumUtils.uppercaseValueOf(TaskPriority.class, priority);
    }

    protected final TaskStatus mapToStatus(String status) {
        return EnumUtils.uppercaseValueOf(TaskStatus.class, status);
    }
}
