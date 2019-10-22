package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.GrpcInformation;

public interface GrpcInformationMapper {
	ValueToModelMapper<GrpcInformation, Entity> toGrpcInformation = entity -> isNotEmpty(entity) ? GrpcInformation.builder()
			.build() : GrpcInformation.builder().build();

	ValueFromModelMapper<GrpcInformation, Entity> fromGrpcInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.build() : Entity.entityBuilder().build();
}
