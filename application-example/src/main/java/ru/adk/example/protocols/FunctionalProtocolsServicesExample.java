package ru.adk.example.protocols;

import ru.adk.core.caster.Caster;
import ru.adk.http.client.model.HttpCommunicationTargetConfiguration;
import static java.lang.Thread.sleep;
import static java.util.function.Function.identity;
import static ru.adk.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.entity.PrimitivesFactory.stringPrimitive;
import static ru.adk.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;
import static ru.adk.grpc.server.GrpcServer.grpcServerInSeparatedThread;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.EXECUTE_GRPC_FUNCTION;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.adk.grpc.server.function.GrpcServiceFunction.grpc;
import static ru.adk.grpc.server.module.GrpcServerModule.grpcServerModule;
import static ru.adk.http.client.communicator.HttpCommunicator.httpCommunicator;
import static ru.adk.http.server.HttpServer.httpServerInSeparatedThread;
import static ru.adk.http.server.function.HttpServiceFunction.httpGet;
import static ru.adk.http.server.module.HttpServerModule.httpServerModule;
import static ru.adk.rsocket.communicator.RsocketCommunicator.rsocketCommunicator;
import static ru.adk.rsocket.constants.RsocketModuleConstants.EXECUTE_RSOCKET_FUNCTION;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RsocketTransport.WEB_SOCKET;
import static ru.adk.rsocket.function.RsocketServiceFunction.rsocket;
import static ru.adk.rsocket.model.RsocketCommunicationTargetConfiguration.rsocketCommunicationTarget;
import static ru.adk.rsocket.module.RsocketModule.rsocketModule;
import static ru.adk.rsocket.server.RsocketServer.rsocketWebSocketServerInSeparatedThread;

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
                .methodId(EXECUTE_GRPC_FUNCTION)
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .execute(stringPrimitive("\u001B[33mHello, GRPC\u001B[0m")));

        rsocketWebSocketServerInSeparatedThread();
        sleep(100L);
        rsocketCommunicator(rsocketCommunicationTarget()
                .host(rsocketModule().getAcceptorHost())
                .port(rsocketModule().getAcceptorWebSocketPort())
                .transport(WEB_SOCKET)
                .build())
                .serviceId(RSOCKET_SERVICE_TYPE)
                .methodId(EXECUTE_RSOCKET_FUNCTION)
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .execute(stringPrimitive("\u001B[34mHello, RSocket\u001B[0m"))
                .subscribe(System.out::println);
    }
}
