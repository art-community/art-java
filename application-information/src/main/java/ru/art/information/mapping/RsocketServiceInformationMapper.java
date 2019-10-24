package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.RsocketServiceInformation;

public interface RsocketServiceInformationMapper {
	String id = "id";

	String methods = "methods";

	ValueToModelMapper<RsocketServiceInformation, Entity> toRsocketServiceInformation = entity -> isNotEmpty(entity) ? RsocketServiceInformation.builder()
			.id(entity.getString(id))
			.methods(entity.getMap(methods, PrimitiveMapping.StringPrimitive.toModel, RsocketServiceMethodInformationMapper.toRsocketServiceMethodInformation))
			.build() : RsocketServiceInformation.builder().build();

	ValueFromModelMapper<RsocketServiceInformation, Entity> fromRsocketServiceInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(id, model.getId())
			.mapField(methods, model.getMethods(), PrimitiveMapping.StringPrimitive.fromModel, RsocketServiceMethodInformationMapper.fromRsocketServiceMethodInformation)
			.build() : Entity.entityBuilder().build();
}
