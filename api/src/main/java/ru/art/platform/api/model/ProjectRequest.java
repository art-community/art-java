package ru.art.platform.api.model;

import lombok.*;

@Value
@Builder
public class ProjectRequest {
    private final String name;
    private final String url;
}
