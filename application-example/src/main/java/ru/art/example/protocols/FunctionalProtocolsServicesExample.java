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

package ru.art.example.protocols;

import ru.art.core.caster.*;
import ru.art.http.client.model.*;

import static java.lang.Thread.*;
import static java.util.function.Function.*;
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.*;
import static ru.art.core.context.Context.*;
import static ru.art.entity.PrimitivesFactory.*;
import static ru.art.grpc.client.communicator.GrpcCommunicator.*;
import static ru.art.grpc.server.GrpcServer.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.grpc.server.function.GrpcServiceFunction.*;
import static ru.art.grpc.server.module.GrpcServerModule.*;
import static ru.art.http.client.communicator.HttpCommunicator.*;
import static ru.art.http.server.HttpServer.*;
import static ru.art.http.server.function.HttpServiceFunction.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.rsocket.communicator.RsocketCommunicator.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.*;
import static ru.art.rsocket.function.RsocketServiceFunction.*;
import static ru.art.rsocket.model.RsocketCommunicationTargetConfiguration.*;
import static ru.art.rsocket.module.RsocketModule.*;
import static ru.art.rsocket.server.RsocketServer.*;

public class FunctionalProtocolsServicesExample {
    public static void main(String[] args) throws InterruptedException {
        useAgileConfigurations();
        httpGet("/test")
                .fromBody()
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .handle(identity());

        grpc(GRPC_SERVICE_TYPE)
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .handle(identity());

        rsocket(RSOCKET_SERVICE_TYPE)
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .handle(identity());

        httpServerInSeparatedThread();
        sleep(100L);
        System.out.println(httpCommunicator(HttpCommunicationTargetConfiguration.builder()
                .host(httpServerModule().getHost())
                .port(httpServerModule().getPort())
                .path("/test")
                .build())
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .execute(stringPrimitive("\u001B[32mHello, HTTP\u001B[0m")));

        grpcServerInSeparatedThread();
        sleep(100L);
        System.out.println(grpcCommunicator(contextConfiguration().getIpAddress(), grpcServerModule().getPort(), grpcServerModule().getPath())
                .serviceId(GRPC_SERVICE_TYPE)
                .methodId(GRPC_FUNCTION_SERVICE)
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .execute(stringPrimitive("\u001B[33mHello, GRPC\u001B[0m")));

        rsocketWebSocketServerInSeparatedThread();
        sleep(100L);
        rsocketCommunicator(rsocketCommunicationTarget()
                .host(rsocketModule().getServerHost())
                .tcpPort(rsocketModule().getServerWebSocketPort())
                .transport(WEB_SOCKET)
                .build())
                .serviceId(RSOCKET_SERVICE_TYPE)
                .methodId(RSOCKET_FUNCTION_SERVICE)
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .execute(stringPrimitive("\u001B[34mHello, RSocket\u001B[0m"))
                .subscribe(System.out::println);
    }
}
