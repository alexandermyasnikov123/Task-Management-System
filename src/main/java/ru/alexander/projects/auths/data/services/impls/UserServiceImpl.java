package ru.alexander.projects.auths.data.services.impls;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexander.projects.auths.data.exceptions.NotUniqueUserException;
import ru.alexander.projects.auths.data.exceptions.UserEmailNotFoundException;
import ru.alexander.projects.auths.data.exceptions.UserNameNotFoundException;
import ru.alexander.projects.auths.data.mappers.UserMapper;
import ru.alexander.projects.auths.data.repositories.UserRepository;
import ru.alexander.projects.auths.domain.models.requests.CreateUserRequest;
import ru.alexander.projects.auths.domain.models.responses.UserResponse;
import ru.alexander.projects.auths.domain.services.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository repository;

    UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (repository.findByUsernameOrEmail(request.username(), request.email()).isPresent()) {
            throw new NotUniqueUserException();
        }

        final var entity = userMapper.mapToEntity(request);
        repository.save(entity);

        return userMapper.mapToUserResponse(entity);
    }

    @Override
    public UserResponse findUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(userMapper::mapToUserResponse)
                .orElseThrow(UserEmailNotFoundException::new);
    }

    @Override
    public UserResponse findUserByUsername(String username) {
        return repository.findByUsername(username)
                .map(userMapper::mapToUserResponse)
                .orElseThrow(UserNameNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        repository.deleteByUsername(username);
    }
}
