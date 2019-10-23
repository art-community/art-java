package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.HttpServiceInformation;

public interface HttpServiceInformationMapper {
	String id = "id";

	String methods = "methods";

	ValueToModelMapper<HttpServiceInformation, Entity> toHttpServiceInformation = entity -> isNotEmpty(entity) ? HttpServiceInformation.builder()
			.id(entity.getString(id))
			.methods(entity.getMap(methods, PrimitiveMapping.StringPrimitive.toModel, HttpServiceMethodInformationMapper.toHttpServiceMethodInformation))
			.build() : HttpServiceInformation.builder().build();

	ValueFromModelMapper<HttpServiceInformation, Entity> fromHttpServiceInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(id, model.getId())
			.mapField(methods, model.getMethods(), PrimitiveMapping.StringPrimitive.fromModel, HttpServiceMethodInformationMapper.fromHttpServiceMethodInformation)
			.build() : Entity.entityBuilder().build();
}
