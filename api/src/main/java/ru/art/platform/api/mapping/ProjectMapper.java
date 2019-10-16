package ru.art.platform.api.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.platform.api.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.platform.api.constants.ApIConstants.*;

public interface ProjectMapper {
    String id = "id";

    String title = "title";

    String gitUrl = "gitUrl";

    String jiraUrl = "jiraUrl";

    String technologies = "technologies";

    String state = "state";

    ValueToModelMapper<Project, Entity> toProject = entity -> isNotEmpty(entity) ? Project.builder()
            .id(entity.getLong(id))
            .title(entity.getString(title))
            .gitUrl(entity.getString(gitUrl))
            .jiraUrl(entity.getString(jiraUrl))
            .technologies(entity.getStringSet(technologies))
            .state(isEmpty(entity.getString(state)) ? ProjectState.NEW : ProjectState.valueOf(entity.getString(state)))
            .build() : Project.builder().build();

    ValueFromModelMapper<Project, Entity> fromProject = model -> isNotEmpty(model) ? Entity.entityBuilder()
            .longField(id, model.getId())
            .stringField(title, model.getTitle())
            .stringField(gitUrl, model.getGitUrl())
            .stringField(jiraUrl, model.getJiraUrl())
            .stringCollectionField(technologies, model.getTechnologies())
            .stringField(state, model.getState().name())
            .build() : Entity.entityBuilder().build();
}
