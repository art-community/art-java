package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.Project;

public interface ProjectMapper {
	String id = "id";

	String name = "name";

	String repositoryUrl = "repositoryUrl";

	ValueToModelMapper<Project, Entity> toProject = entity -> isNotEmpty(entity) ? Project.builder()
			.id(entity.getLong(id))
			.name(entity.getString(name))
			.repositoryUrl(entity.getString(repositoryUrl))
			.build() : Project.builder().build();

	ValueFromModelMapper<Project, Entity> fromProject = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.longField(id, model.getId())
			.stringField(name, model.getName())
			.stringField(repositoryUrl, model.getRepositoryUrl())
			.build() : Entity.entityBuilder().build();
}
