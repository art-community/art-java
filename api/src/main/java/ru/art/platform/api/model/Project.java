package ru.art.platform.api.model;

import lombok.*;

@Value
@Builder
public class Project {
    private final Long id;
    private final String name;
    private final String repositoryUrl;
}
