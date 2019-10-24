package ru.art.information.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.information.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface InformationResponseMapper {
	String grpcInformation = "grpcInformation";

	String httpInformation = "httpInformation";

	String rsocketInformation = "rsocketInformation";

	String statusResponse = "statusResponse";

	ValueToModelMapper<InformationResponse, Entity> toInformationResponse = entity -> isNotEmpty(entity) ? InformationResponse.builder()
			.grpcInformation(entity.getValue(grpcInformation, GrpcInformationMapper.toGrpcInformation))
			.httpInformation(entity.getValue(httpInformation, HttpInformationMapper.toHttpInformation))
			.rsocketInformation(entity.getValue(rsocketInformation, RsocketInformationMapper.toRsocketInformation))
			.statusResponse(entity.getValue(statusResponse, StatusResponseMapper.toStatusResponse))
			.build() : InformationResponse.builder().build();

	ValueFromModelMapper<InformationResponse, Entity> fromInformationResponse = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.entityField(grpcInformation, model.getGrpcInformation(), GrpcInformationMapper.fromGrpcInformation)
			.entityField(httpInformation, model.getHttpInformation(), HttpInformationMapper.fromHttpInformation)
			.entityField(rsocketInformation, model.getRsocketInformation(), RsocketInformationMapper.fromRsocketInformation)
			.entityField(statusResponse, model.getStatusResponse(), StatusResponseMapper.fromStatusResponse)
			.build() : Entity.entityBuilder().build();
}
