package ru.alexander.projects.shared.data.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.util.Pair;
import ru.alexander.projects.shared.infrastrusture.CustomMessageSource;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalizedRuntimeException extends RuntimeException {
    String causeCode;

    String messageCode;

    /**
     * @deprecated <p>use {@link LocalizedRuntimeException#toString(CustomMessageSource)} instead</p>
     **/
    @Override
    @Deprecated(since = "use toString(messageSource) instead")
    public final String toString() {
        return super.toString();
    }

    /**
     * @deprecated <p>use {@link CustomMessageSource#getMessage(String)} instead</p>
     **/
    @Override
    @Deprecated
    public final String getMessage() {
        return super.getMessage();
    }

    /**
     * @deprecated <p>use {@link CustomMessageSource#getMessage(String)} instead</p>
     **/
    @Override
    @Deprecated
    public final String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    public String toString(CustomMessageSource messageSource) {
        return messageSource.getMessage(getCauseCode()) + ": " + messageSource.getMessage(getMessageCode());
    }

    public Pair<String, String> toMessagePair(CustomMessageSource messageSource) {
        return Pair.of(messageSource.getMessage(getCauseCode()), messageSource.getMessage(getMessageCode()));
    }
}
