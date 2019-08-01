package ru.adk.grpc.server.state;

import lombok.Getter;
import lombok.Setter;
import ru.adk.core.module.ModuleState;
import ru.adk.grpc.server.GrpcServer;

@Getter
@Setter
public class GrpcServerModuleState implements ModuleState {
    private GrpcServer server;
}
