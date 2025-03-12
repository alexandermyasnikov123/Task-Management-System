package ru.alexander.projects.comments.data.mappers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.alexander.projects.comments.data.entities.CommentEntity;
import ru.alexander.projects.comments.domain.models.requests.CreateCommentRequest;
import ru.alexander.projects.comments.domain.models.responses.CommentResponse;
import ru.alexander.projects.shared.data.mappers.BaseMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class CommentMapper extends BaseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "task", expression = "java(getTask(request.taskId()))")
    @Mapping(target = "owner", expression = "java(getCurrentUser())")
    public abstract CommentEntity mapToEntity(CreateCommentRequest request);

    @Mapping(target = "taskId", source = "entity.task.id")
    @Mapping(target = "ownerId", source = "entity.owner.id")
    public abstract CommentResponse mapToResponse(CommentEntity entity);
}
