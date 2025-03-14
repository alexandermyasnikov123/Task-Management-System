package ru.alexander.projects.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.alexander.projects.shared.data.exceptions.InputOutOfRangeException;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtils {

    public static <E extends Enum<E>> E uppercaseValueOf(Class<E> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            final var availableInput = Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .toList();

            throw new InputOutOfRangeException(availableInput);
        }
    }
}
