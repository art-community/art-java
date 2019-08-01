package ru.art.module.executor.service;

import ru.art.core.caster.Caster;
import ru.art.entity.Value;
import ru.art.module.executor.model.ExecutionRequest;
import static ru.art.core.caster.Caster.cast;
import static ru.art.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;


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
        return cast(grpcCommunicator(request.getModuleHost(), request.getModulePort(), request.getExecutableServletPath())
                .serviceId(request.getExecutableServiceId())
                .methodId(request.getExecutableMethodId())
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .execute(request.getExecutableRequest()).getResponseData());
    }
}