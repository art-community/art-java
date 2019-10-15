package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.Project;

public interface ProjectMapper {
	String id = "id";

	String title = "title";

	String gitUrl = "gitUrl";

	String jiraUrl = "jiraUrl";

	ValueToModelMapper<Project, Entity> toProject = entity -> isNotEmpty(entity) ? Project.builder()
			.id(entity.getLong(id))
			.title(entity.getString(title))
			.gitUrl(entity.getString(gitUrl))
			.jiraUrl(entity.getString(jiraUrl))
			.build() : Project.builder().build();

	ValueFromModelMapper<Project, Entity> fromProject = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.longField(id, model.getId())
			.stringField(title, model.getTitle())
			.stringField(gitUrl, model.getGitUrl())
			.stringField(jiraUrl, model.getJiraUrl())
			.build() : Entity.entityBuilder().build();
}
