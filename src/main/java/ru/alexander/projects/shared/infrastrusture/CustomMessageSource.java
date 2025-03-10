package ru.alexander.projects.shared.infrastrusture;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

public interface CustomMessageSource extends MessageSource {

    /// Alias for getMessage(code, null, LocaleContextHolder.getLocale())
    String getMessage(String code);

    /// Alias for getMessage(code, args, LocaleContextHolder.getLocale())
    String getMessage(String code, Object... args);

    @Primary
    @Component
    @RequiredArgsConstructor
    class DelegateMessageSource implements CustomMessageSource {
        @Delegate
        private final MessageSource messageSource;

        @Override
        public String getMessage(String code) {
            return getMessage(code, null, LocaleContextHolder.getLocale());
        }

        @Override
        public String getMessage(String code, Object... args) {
            return getMessage(code, args, LocaleContextHolder.getLocale());
        }
    }
}
