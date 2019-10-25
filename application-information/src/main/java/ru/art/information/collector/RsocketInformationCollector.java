/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.information.collector;

import lombok.experimental.*;
import ru.art.information.generator.*;
import ru.art.information.model.*;
import ru.art.rsocket.specification.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.network.provider.IpAddressProvider.*;
import static ru.art.information.model.RsocketInformation.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.module.RsocketModule.*;
import static ru.art.service.ServiceModule.*;

@UtilityClass
public class RsocketInformationCollector {
    public static RsocketInformation collectRsocketInformation() {
        if (!context().hasModule(RSOCKET_MODULE_ID)) {
            return null;
        }
        boolean tcpServerNotStarted = isNull(rsocketModuleState().getTcpServer()) || !rsocketModuleState().getTcpServer().isWorking();
        boolean webSocketServerNotStated = isNull(rsocketModuleState().getWebSocketServer()) || !rsocketModuleState().getWebSocketServer().isWorking();
        if (tcpServerNotStarted && webSocketServerNotStated) {
            return null;
        }
        RsocketInformationBuilder builder = builder();
        if (!tcpServerNotStarted) {
            builder.tcpUrl(getIpAddress() + COLON + rsocketModule().getServerTcpPort());
        }
        if (!webSocketServerNotStated) {
            builder.webSocketUrl(getIpAddress() + COLON + rsocketModule().getServerWebSocketPort());
        }
        return builder.services(serviceModuleState()
                .getServiceRegistry()
                .getServices()
                .values()
                .stream()
                .filter(service -> service.getServiceTypes().contains(RSOCKET_SERVICE_TYPE))
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
