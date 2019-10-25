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

package ru.art.information.service;

import lombok.experimental.*;
import ru.art.information.model.*;
import ru.art.information.model.StatusResponse.*;
import static java.util.Objects.*;
import static ru.art.core.context.Context.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.grpc.server.module.GrpcServerModule.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.information.collector.GrpcInformationCollector.*;
import static ru.art.information.collector.HttpInformationCollector.*;
import static ru.art.information.collector.RsocketInformationCollector.*;
import static ru.art.information.model.InformationResponse.*;
import static ru.art.information.module.InformationModule.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.module.RsocketModule.*;

@UtilityClass
public class InformationService {
    public static InformationResponse getInformation() {
        StatusResponse status = getStatus();
        InformationResponseBuilder information = InformationResponse.builder().statusResponse(status);
        if (status.isHttp()) {
            information.httpInformation(collectHttpInformation());
        }
        if (status.isGrpc()) {
            information.grpcInformation(collectGrpcInformation());
        }
        if (status.isRsocketTcp() || status.isRsocketWebSocket()) {
            information.rsocketInformation(collectRsocketInformation());
        }
        return information.build();
    }

    public static StatusResponse getStatus() {
        StatusResponseBuilder response = StatusResponse.builder();
        if (isNull(informationModule().getStatusSupplier())) {
            if (context().hasModule(HTTP_SERVER_MODULE_ID) && nonNull(httpServerModuleState().getServer()) && httpServerModuleState().getServer().isWorking()) {
                response.http(true);
            }
            if (context().hasModule(GRPC_SERVER_MODULE_ID) && nonNull(grpcServerModuleState().getServer()) && grpcServerModuleState().getServer().isWorking()) {
                response.grpc(true);
            }
            if (context().hasModule(RSOCKET_MODULE_ID) && nonNull(rsocketModuleState().getWebSocketServer()) && rsocketModuleState().getWebSocketServer().isWorking()) {
                response.rsocketWebSocket(true);
            }
            if (context().hasModule(RSOCKET_MODULE_ID) && nonNull(rsocketModuleState().getTcpServer()) && rsocketModuleState().getTcpServer().isWorking()) {
                response.rsocketWebSocket(true);
            }
            return response.build();
        }
        return informationModule().getStatusSupplier().get();
    }
}
