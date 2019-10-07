package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.Module;

public interface ModuleMapper {
	String id = "id";

	String name = "name";

	ValueToModelMapper<Module, Entity> toModule = entity -> isNotEmpty(entity) ? Module.builder()
			.id(entity.getLong(id))
			.name(entity.getString(name))
			.build() : Module.builder().build();

	ValueFromModelMapper<Module, Entity> fromModule = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.longField(id, model.getId())
			.stringField(name, model.getName())
			.build() : Entity.entityBuilder().build();
}
