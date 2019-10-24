package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.GrpcServiceInformation;

public interface GrpcServiceInformationMapper {
	String id = "id";

	String methods = "methods";

	ValueToModelMapper<GrpcServiceInformation, Entity> toGrpcServiceInformation = entity -> isNotEmpty(entity) ? GrpcServiceInformation.builder()
			.id(entity.getString(id))
			.methods(entity.getMap(methods, PrimitiveMapping.StringPrimitive.toModel, GrpcServiceMethodInformationMapper.toGrpcServiceMethodInformation))
			.build() : GrpcServiceInformation.builder().build();

	ValueFromModelMapper<GrpcServiceInformation, Entity> fromGrpcServiceInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(id, model.getId())
			.mapField(methods, model.getMethods(), PrimitiveMapping.StringPrimitive.fromModel, GrpcServiceMethodInformationMapper.fromGrpcServiceMethodInformation)
			.build() : Entity.entityBuilder().build();
}
