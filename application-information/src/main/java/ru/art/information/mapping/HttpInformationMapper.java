package ru.art.information.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.information.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface HttpInformationMapper {
	String services = "services";

	ValueToModelMapper<HttpInformation, Entity> toHttpInformation = entity -> isNotEmpty(entity) ? HttpInformation.builder()
			.services(entity.getMap(services, PrimitiveMapping.StringPrimitive.toModel, HttpServiceInformationMapper.toHttpServiceInformation))
			.build() : HttpInformation.builder().build();

	ValueFromModelMapper<HttpInformation, Entity> fromHttpInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.mapField(services, model.getServices(), PrimitiveMapping.StringPrimitive.fromModel, HttpServiceInformationMapper.fromHttpServiceInformation)
			.build() : Entity.entityBuilder().build();
}
