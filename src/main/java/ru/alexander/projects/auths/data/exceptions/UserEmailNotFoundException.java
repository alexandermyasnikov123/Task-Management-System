package ru.alexander.projects.auths.data.exceptions;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

@StandardException
public class UserEmailNotFoundException extends AuthenticationException {
}
