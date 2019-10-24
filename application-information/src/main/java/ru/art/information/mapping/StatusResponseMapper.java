package ru.art.information.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.information.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface StatusResponseMapper {
	String http = "http";

	String grpc = "grpc";

	String rsocketTcp = "rsocketTcp";

	String rsocketWebSocket = "rsocketWebSocket";

	ValueToModelMapper<StatusResponse, Entity> toStatusResponse = entity -> isNotEmpty(entity) ? StatusResponse.builder()
			.http(entity.getBool(http))
			.grpc(entity.getBool(grpc))
			.rsocketTcp(entity.getBool(rsocketTcp))
			.rsocketWebSocket(entity.getBool(rsocketWebSocket))
			.build() : StatusResponse.builder().build();

	ValueFromModelMapper<StatusResponse, Entity> fromStatusResponse = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.boolField(http, model.isHttp())
			.boolField(grpc, model.isGrpc())
			.boolField(rsocketTcp, model.isRsocketTcp())
			.boolField(rsocketWebSocket, model.isRsocketWebSocket())
			.build() : Entity.entityBuilder().build();
}
