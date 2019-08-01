package ru.adk.core.caster;

public interface Caster {
    @SuppressWarnings("unchecked")
    static <T> T cast(Object obj) {
        return (T) obj;
    }
}
