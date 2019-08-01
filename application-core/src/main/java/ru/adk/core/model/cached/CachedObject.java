package ru.adk.core.model.cached;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class CachedObject<T> {
    private T object;
    private long lastUpdateTime;
}
