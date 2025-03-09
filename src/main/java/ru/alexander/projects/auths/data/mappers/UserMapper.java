package ru.alexander.projects.auths.data.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alexander.projects.auths.data.entities.UserDetailsEntity;
import ru.alexander.projects.auths.domain.models.requests.CreateUserRequest;
import ru.alexander.projects.auths.domain.models.requests.RegisterRequest;
import ru.alexander.projects.auths.domain.models.responses.AuthResponse;
import ru.alexander.projects.auths.domain.models.responses.UserResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    @Autowired
    protected PasswordEncoder encoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", expression = "java(encoder.encode(request.password()))")
    public abstract UserDetailsEntity mapToEntity(CreateUserRequest request);

    public abstract UserResponse mapToUserResponse(UserDetailsEntity entity);

    public abstract AuthResponse mapToAuthResponse(UserResponse response, String jwtToken);

    public abstract CreateUserRequest mapToCreateRequest(RegisterRequest request);
}
