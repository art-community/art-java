package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.HttpInformation;

public interface HttpInformationMapper {
	String services = "services";

	ValueToModelMapper<HttpInformation, Entity> toHttpInformation = entity -> isNotEmpty(entity) ? HttpInformation.builder()
			.services(entity.getMap(services, PrimitiveMapping.StringPrimitive.toModel, HttpServiceInformationMapper.toHttpServiceInformation))
			.build() : HttpInformation.builder().build();

	ValueFromModelMapper<HttpInformation, Entity> fromHttpInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.mapField(services, model.getServices(), PrimitiveMapping.StringPrimitive.fromModel, HttpServiceInformationMapper.fromHttpServiceInformation)
			.build() : Entity.entityBuilder().build();
}
