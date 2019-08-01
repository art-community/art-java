package ru.adk.core.extension;

import java.util.Objects;

public interface EqualsCheckingExtensions {
    static <T> T ifEquals(T val, T pattern, T ifEquals) {
        return Objects.equals(val, pattern) ? ifEquals : val;
    }

    static <T> T ifNotEquals(T val, T pattern, T ifNotEquals) {
        return !Objects.equals(val, pattern) ? ifNotEquals : val;
    }
}
