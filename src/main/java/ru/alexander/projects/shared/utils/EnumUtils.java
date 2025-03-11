package ru.alexander.projects.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtils {

    public static <E extends Enum<E>> E uppercaseValueOf(Class<E> enumClass, String value) {
        return Enum.valueOf(enumClass, value.trim().toUpperCase());
    }
}
