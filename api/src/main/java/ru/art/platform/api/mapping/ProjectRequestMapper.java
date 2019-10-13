package ru.art.platform.api.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.platform.api.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface ProjectRequestMapper {
	String name = "name";

	String gitUrl = "gitUrl";

	String jiraUrl = "jiraUrl";

	ValueToModelMapper<ProjectRequest, Entity> toProjectRequest = entity -> isNotEmpty(entity) ? ProjectRequest.builder()
			.name(entity.getString(name))
			.gitUrl(entity.getString(gitUrl))
			.jiraUrl(entity.getString(jiraUrl))
			.build() : ProjectRequest.builder().build();

	ValueFromModelMapper<ProjectRequest, Entity> fromProjectRequest = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(name, model.getName())
			.stringField(gitUrl, model.getGitUrl())
			.stringField(jiraUrl, model.getJiraUrl())
			.build() : Entity.entityBuilder().build();
}
