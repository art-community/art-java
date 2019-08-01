package ru.art.grpc.server.state;

import lombok.Getter;
import lombok.Setter;
import ru.art.core.module.ModuleState;
import ru.art.grpc.server.GrpcServer;

@Getter
@Setter
public class GrpcServerModuleState implements ModuleState {
    private GrpcServer server;
}
