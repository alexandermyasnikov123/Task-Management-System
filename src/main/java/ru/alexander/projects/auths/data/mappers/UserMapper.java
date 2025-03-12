package ru.alexander.projects.auths.data.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alexander.projects.auths.data.entities.UserEntity;
import ru.alexander.projects.auths.data.entities.UserRole;
import ru.alexander.projects.auths.domain.models.requests.CreateUserRequest;
import ru.alexander.projects.auths.domain.models.requests.RegisterRequest;
import ru.alexander.projects.auths.domain.models.responses.AuthResponse;
import ru.alexander.projects.auths.domain.models.responses.UserResponse;
import ru.alexander.projects.shared.utils.EnumUtils;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    @Autowired
    protected PasswordEncoder encoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(encoder.encode(request.password()))")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdTasks", ignore = true)
    @Mapping(target = "tasksToComplete", ignore = true)
    public abstract UserEntity mapToEntity(CreateUserRequest request);

    @Mapping(target = "role", expression = "java(mapUserRole(request))")
    public abstract CreateUserRequest mapToRequest(RegisterRequest request);

    public abstract UserResponse mapToUserResponse(UserEntity entity);

    public abstract AuthResponse mapToAuthResponse(UserResponse response, String jwtToken);

    protected UserRole mapUserRole(RegisterRequest request) {
        return EnumUtils.uppercaseValueOf(UserRole.class, request.getRole());
    }
}
