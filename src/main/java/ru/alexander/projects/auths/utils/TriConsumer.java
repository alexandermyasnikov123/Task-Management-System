package ru.alexander.projects.auths.utils;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T1, T2, T3> {

    void accept(T1 first, T2 second, T3 third);

    default TriConsumer<T1, T2, T3> andThen(TriConsumer<? super T1, ? super T2, ? super T3> other) {
        Objects.requireNonNull(other);
        return (first, second, third) -> {
            this.accept(first, second, third);
            other.accept(first, second, third);
        };
    }
}
