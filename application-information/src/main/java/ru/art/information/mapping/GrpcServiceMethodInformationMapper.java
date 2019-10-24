package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.GrpcServiceMethodInformation;

public interface GrpcServiceMethodInformationMapper {
	String id = "id";

	String exampleRequest = "exampleRequest";

	String exampleResponse = "exampleResponse";

	ValueToModelMapper<GrpcServiceMethodInformation, Entity> toGrpcServiceMethodInformation = entity -> isNotEmpty(entity) ? GrpcServiceMethodInformation.builder()
			.id(entity.getString(id))
			.exampleRequest(entity.getString(exampleRequest))
			.exampleResponse(entity.getString(exampleResponse))
			.build() : GrpcServiceMethodInformation.builder().build();

	ValueFromModelMapper<GrpcServiceMethodInformation, Entity> fromGrpcServiceMethodInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(id, model.getId())
			.stringField(exampleRequest, model.getExampleRequest())
			.stringField(exampleResponse, model.getExampleResponse())
			.build() : Entity.entityBuilder().build();
}
