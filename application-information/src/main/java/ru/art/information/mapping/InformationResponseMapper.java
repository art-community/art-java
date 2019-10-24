package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.InformationResponse;

public interface InformationResponseMapper {
	String grpcInformation = "grpcInformation";

	String httpInformation = "httpInformation";

	String rsocketInformation = "rsocketInformation";

	ValueToModelMapper<InformationResponse, Entity> toInformationResponse = entity -> isNotEmpty(entity) ? InformationResponse.builder()
			.grpcInformation(entity.getValue(grpcInformation, GrpcInformationMapper.toGrpcInformation))
			.httpInformation(entity.getValue(httpInformation, HttpInformationMapper.toHttpInformation))
			.rsocketInformation(entity.getValue(rsocketInformation, RsocketInformationMapper.toRsocketInformation))
			.build() : InformationResponse.builder().build();

	ValueFromModelMapper<InformationResponse, Entity> fromInformationResponse = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.entityField(grpcInformation, model.getGrpcInformation(), GrpcInformationMapper.fromGrpcInformation)
			.entityField(httpInformation, model.getHttpInformation(), HttpInformationMapper.fromHttpInformation)
			.entityField(rsocketInformation, model.getRsocketInformation(), RsocketInformationMapper.fromRsocketInformation)
			.build() : Entity.entityBuilder().build();
}
