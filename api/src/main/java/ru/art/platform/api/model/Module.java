package ru.art.platform.api.model;

import lombok.*;

@Value
@Builder
public class Module {
    private final Long id;
    private final String name;
}
