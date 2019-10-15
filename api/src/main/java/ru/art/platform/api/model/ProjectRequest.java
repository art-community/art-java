package ru.art.platform.api.model;

import lombok.*;

@Value
@Builder
public class ProjectRequest {
    private final String title;
    private final String gitUrl;
    private final String jiraUrl;
}
