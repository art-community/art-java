package ru.art.platform.api.model;

import lombok.*;
import java.util.*;

@Value
@Builder(toBuilder = true)
public class Project {
    private final Long id;
    private final String title;
    private final String gitUrl;
    private final String jiraUrl;
    @Singular("technology")
    private final Set<String> technologies;
}
