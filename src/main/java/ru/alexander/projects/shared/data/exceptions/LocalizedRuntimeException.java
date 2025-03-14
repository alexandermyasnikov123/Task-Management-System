package ru.alexander.projects.shared.data.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexander.projects.shared.infrastrusture.CustomMessageSource;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class LocalizedRuntimeException extends RuntimeException {
    @NonNull
    String causeCode;

    @NonNull
    String messageCode;

    List<?> messageArguments;

    public String toString(CustomMessageSource messageSource) {
        final var messagePair = toMessagePair(messageSource);
        return messagePair.getKey() + ": " + messagePair.getValue();
    }

    public Pair<String, String> toMessagePair(CustomMessageSource messageSource) {
        final var cause = messageSource.getMessage(getCauseCode());
        final var message = messageSource.getMessage(getMessageCode(), getMessageArguments());
        return Pair.of(cause, message);
    }

    /// @deprecated <p>use {@link LocalizedRuntimeException#toString(CustomMessageSource)} instead
    @Override
    @Deprecated(since = "use toString(messageSource) instead")
    public final String toString() {
        return super.toString();
    }

    /// @deprecated <p>use {@link CustomMessageSource#getMessage(String)} instead</p>
    @Override
    @Deprecated
    public final String getMessage() {
        return super.getMessage();
    }

    /// @deprecated <p>use {@link CustomMessageSource#getMessage(String)} instead</p>
    @Override
    @Deprecated
    public final String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
