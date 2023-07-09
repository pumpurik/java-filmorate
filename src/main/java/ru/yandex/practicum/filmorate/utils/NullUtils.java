package ru.yandex.practicum.filmorate.utils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class NullUtils {
    public static <F> F getOrNull(Callable<F> callable) {
        return getOrDefault(callable, null);
    }

    public static <F> F getOrDefault(Callable<F> callable, F defaultValue) {
        try {
            return callable.call();
        } catch (Throwable e) {

        }
        return defaultValue;
    }

    public static <T> List<T> nonNullList(List<T> list) {
        return (list == null) ? List.of() : list;
    }

    public static <T> Set<T> nonNullSet(Set<T> set) {
        return (set == null) ? Set.of() : set;
    }
}
