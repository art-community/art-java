package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.RsocketInformation;

public interface RsocketInformationMapper {
	String webSocketUrl = "webSocketUrl";

	String tcpUrl = "tcpUrl";

	String services = "services";

	ValueToModelMapper<RsocketInformation, Entity> toRsocketInformation = entity -> isNotEmpty(entity) ? RsocketInformation.builder()
			.webSocketUrl(entity.getString(webSocketUrl))
			.tcpUrl(entity.getString(tcpUrl))
			.services(entity.getMap(services, PrimitiveMapping.StringPrimitive.toModel, RsocketServiceInformationMapper.toRsocketServiceInformation))
			.build() : RsocketInformation.builder().build();

	ValueFromModelMapper<RsocketInformation, Entity> fromRsocketInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(webSocketUrl, model.getWebSocketUrl())
			.stringField(tcpUrl, model.getTcpUrl())
			.mapField(services, model.getServices(), PrimitiveMapping.StringPrimitive.fromModel, RsocketServiceInformationMapper.fromRsocketServiceInformation)
			.build() : Entity.entityBuilder().build();
}
