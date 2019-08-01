package ru.art.core.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IdentifiedValue<T> {
    private String id;
    private T value;
}
