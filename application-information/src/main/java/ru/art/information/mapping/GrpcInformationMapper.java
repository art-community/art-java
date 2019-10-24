package ru.art.information.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.information.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface GrpcInformationMapper {
	String url = "url";

	String services = "services";

	ValueToModelMapper<GrpcInformation, Entity> toGrpcInformation = entity -> isNotEmpty(entity) ? GrpcInformation.builder()
			.url(entity.getString(url))
			.services(entity.getMap(services, PrimitiveMapping.StringPrimitive.toModel, GrpcServiceInformationMapper.toGrpcServiceInformation))
			.build() : GrpcInformation.builder().build();

	ValueFromModelMapper<GrpcInformation, Entity> fromGrpcInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(url, model.getUrl())
			.mapField(services, model.getServices(), PrimitiveMapping.StringPrimitive.fromModel, GrpcServiceInformationMapper.fromGrpcServiceInformation)
			.build() : Entity.entityBuilder().build();
}
