package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.HttpServiceMethodInformation;

public interface HttpServiceMethodInformationMapper {
	String id = "id";

	String url = "url";

	String exampleRequest = "exampleRequest";

	String exampleResponse = "exampleResponse";

	ValueToModelMapper<HttpServiceMethodInformation, Entity> toHttpServiceMethodInformation = entity -> isNotEmpty(entity) ? HttpServiceMethodInformation.builder()
			.id(entity.getString(id))
			.url(entity.getString(url))
			.exampleRequest(entity.getString(exampleRequest))
			.exampleResponse(entity.getString(exampleResponse))
			.build() : HttpServiceMethodInformation.builder().build();

	ValueFromModelMapper<HttpServiceMethodInformation, Entity> fromHttpServiceMethodInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(id, model.getId())
			.stringField(url, model.getUrl())
			.stringField(exampleRequest, model.getExampleRequest())
			.stringField(exampleResponse, model.getExampleResponse())
			.build() : Entity.entityBuilder().build();
}
