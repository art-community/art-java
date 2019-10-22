package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.RsocketInformation;

public interface RsocketInformationMapper {
	ValueToModelMapper<RsocketInformation, Entity> toRsocketInformation = entity -> isNotEmpty(entity) ? RsocketInformation.builder()
			.build() : RsocketInformation.builder().build();

	ValueFromModelMapper<RsocketInformation, Entity> fromRsocketInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.build() : Entity.entityBuilder().build();
}
