package ru.art.platform.api.model;

import lombok.*;
import static ru.art.platform.api.constants.ApIConstants.*;
import static ru.art.platform.api.constants.ApIConstants.ProjectState.*;
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
    @Builder.Default
    private final ProjectState state = NEW;
}
