package ru.art.information.collector;

import lombok.experimental.*;
import ru.art.information.generator.*;
import ru.art.information.model.*;
import ru.art.rsocket.specification.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.network.provider.IpAddressProvider.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.module.RsocketModule.*;
import static ru.art.service.ServiceModule.*;

@UtilityClass
public class RsocketInformationCollector {
    public static RsocketInformation collectRsocketInformation() {
        if (!context().hasModule(RSOCKET_MODULE_ID) || !rsocketModuleState().getServer().isWorking()) {
            return null;
        }
        return RsocketInformation.builder()
                .webSocketUrl(getIpAddress() + COLON + rsocketModule().getServerWebSocketPort())
                .tcpUrl(getIpAddress() + COLON + rsocketModule().getServerTcpPort())
                .services(serviceModuleState()
                        .getServiceRegistry()
                        .getServices()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().getServiceTypes().contains(RSOCKET_SERVICE_TYPE))
                        .map(service -> (RsocketServiceSpecification) service)
                        .map(service -> RsocketServiceInformation
                                .builder()
                                .id(service.getServiceId())
                                .methods(service.getRsocketService()
                                        .getRsocketMethods()
                                        .entrySet()
                                        .stream()
                                        .map(entry -> RsocketServiceMethodInformation
                                                .builder()
                                                .id(entry.getKey())
                                                .exampleRequest(doIfNotNull(entry.getValue().requestMapper(), ExampleJsonGenerator::generateExampleJson, () -> EMPTY_STRING))
                                                .exampleResponse(doIfNotNull(entry.getValue().responseMapper(), ExampleJsonGenerator::generateExampleJson, () -> EMPTY_STRING))
                                                .build())
                                        .collect(toMap(RsocketServiceMethodInformation::getId, identity()))
                                )
                                .build())
                        .collect(toMap(RsocketServiceInformation::getId, identity())))
                .build();
    }
}
