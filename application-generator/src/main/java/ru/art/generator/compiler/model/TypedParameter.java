package ru.art.generator.compiler.model;

import lombok.*;

@Getter
@Builder
public class TypedParameter {
    private final Class<?> type;
    private final String parameter;
}
