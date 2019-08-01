package ru.adk.module.executor.service;

import ru.adk.core.caster.Caster;
import ru.adk.entity.Value;
import ru.adk.module.executor.model.ExecutionRequest;
import static ru.adk.core.extension.OptionalExtensions.unwrap;
import static ru.adk.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;


/**
 * Main service with module executing method
 */
public interface ModuleExecutorService {

    /**
     * Method sends request to remote module by it's host/port with needed request
     *
     * @param request - data about executing module service method with request
     * @return method response
     */
    static Value executeModule(ExecutionRequest request) {
        return unwrap(grpcCommunicator(request.getModuleHost(), request.getModulePort(), request.getExecutableServletPath())
                .serviceId(request.getExecutableServiceId())
                .methodId(request.getExecutableMethodId())
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .execute(request.getExecutableRequest()));
    }
}