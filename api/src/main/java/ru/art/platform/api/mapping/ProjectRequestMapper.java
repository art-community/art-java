package ru.art.platform.api.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.platform.api.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface ProjectRequestMapper {
	String name = "name";

	String url = "url";

	ValueToModelMapper<ProjectRequest, Entity> toProjectRequest = entity -> isNotEmpty(entity) ? ProjectRequest.builder()
			.name(entity.getString(name))
			.url(entity.getString(url))
			.build() : ProjectRequest.builder().build();

	ValueFromModelMapper<ProjectRequest, Entity> fromProjectRequest = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(name, model.getName())
			.stringField(url, model.getUrl())
			.build() : Entity.entityBuilder().build();
}
