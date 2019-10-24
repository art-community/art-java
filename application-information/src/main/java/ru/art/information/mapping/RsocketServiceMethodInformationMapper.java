package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.RsocketServiceMethodInformation;

public interface RsocketServiceMethodInformationMapper {
	String id = "id";

	String exampleRequest = "exampleRequest";

	String exampleResponse = "exampleResponse";

	ValueToModelMapper<RsocketServiceMethodInformation, Entity> toRsocketServiceMethodInformation = entity -> isNotEmpty(entity) ? RsocketServiceMethodInformation.builder()
			.id(entity.getString(id))
			.exampleRequest(entity.getString(exampleRequest))
			.exampleResponse(entity.getString(exampleResponse))
			.build() : RsocketServiceMethodInformation.builder().build();

	ValueFromModelMapper<RsocketServiceMethodInformation, Entity> fromRsocketServiceMethodInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(id, model.getId())
			.stringField(exampleRequest, model.getExampleRequest())
			.stringField(exampleResponse, model.getExampleResponse())
			.build() : Entity.entityBuilder().build();
}
