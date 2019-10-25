package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.BuildRequest;

public interface BuildRequestMapper {
	String projectId = "projectId";

	ValueToModelMapper<BuildRequest, Entity> toBuildRequest = entity -> isNotEmpty(entity) ? BuildRequest.builder()
			.projectId(entity.getLong(projectId))
			.build() : BuildRequest.builder().build();

	ValueFromModelMapper<BuildRequest, Entity> fromBuildRequest = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.longField(projectId, model.getProjectId())
			.build() : Entity.entityBuilder().build();
}
