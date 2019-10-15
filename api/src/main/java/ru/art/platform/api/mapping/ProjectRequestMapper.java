package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.ProjectRequest;

public interface ProjectRequestMapper {
	String title = "title";

	String gitUrl = "gitUrl";

	String jiraUrl = "jiraUrl";

	ValueToModelMapper<ProjectRequest, Entity> toProjectRequest = entity -> isNotEmpty(entity) ? ProjectRequest.builder()
			.title(entity.getString(title))
			.gitUrl(entity.getString(gitUrl))
			.jiraUrl(entity.getString(jiraUrl))
			.build() : ProjectRequest.builder().build();

	ValueFromModelMapper<ProjectRequest, Entity> fromProjectRequest = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(title, model.getTitle())
			.stringField(gitUrl, model.getGitUrl())
			.stringField(jiraUrl, model.getJiraUrl())
			.build() : Entity.entityBuilder().build();
}
