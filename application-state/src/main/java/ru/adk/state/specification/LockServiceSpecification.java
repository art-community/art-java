package ru.adk.state.specification;

import lombok.Getter;
import ru.adk.grpc.server.model.GrpcService;
import ru.adk.grpc.server.specification.GrpcServiceSpecification;
import ru.adk.http.server.model.HttpService;
import ru.adk.http.server.specification.HttpServiceSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.entity.CollectionMapping.stringCollectionMapper;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.adk.grpc.server.model.GrpcService.GrpcMethod.grpcMethod;
import static ru.adk.grpc.server.model.GrpcService.grpcService;
import static ru.adk.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.adk.http.server.model.HttpService.httpService;
import static ru.adk.http.server.module.HttpServerModule.httpServerModule;
import static ru.adk.service.constants.RequestValidationPolicy.VALIDATABLE;
import static ru.adk.state.api.constants.StateApiConstants.LockServiceConstants.LOCK_SERVICE_ID;
import static ru.adk.state.api.constants.StateApiConstants.LockServiceConstants.Methods.*;
import static ru.adk.state.api.constants.StateApiConstants.LockServiceConstants.Paths.*;
import static ru.adk.state.api.mapping.LockRequestMapper.toLockRequest;
import static ru.adk.state.service.LockService.lock;
import static ru.adk.state.service.LockService.unlock;
import java.util.List;

@Getter
public class LockServiceSpecification implements HttpServiceSpecification, GrpcServiceSpecification {
    private final List<String> serviceTypes = fixedArrayOf(HTTP_SERVICE_TYPE, GRPC_SERVICE_TYPE);
    private final String serviceId = LOCK_SERVICE_ID;
    private final HttpService httpService = httpService()
            .post(LOCK)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(toLockRequest)
            .produces(applicationJsonUtf8())
            .listen(LOCK_PATH)

            .post(UNLOCK)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(toLockRequest)
            .produces(applicationJsonUtf8())
            .listen(UNLOCK_PATH)

            .get(GET_CURRENT_LOCKS)
            .produces(applicationJsonUtf8())
            .responseMapper(stringCollectionMapper.getFromModel())
            .listen(GET_CURRENT_LOCKS_PATH)

            .serve(httpServerModule().getPath());

    private final GrpcService grpcService = grpcService()
            .method(LOCK, grpcMethod()
                    .requestMapper(toLockRequest)
                    .validationPolicy(VALIDATABLE))
            .method(UNLOCK, grpcMethod()
                    .requestMapper(toLockRequest)
                    .validationPolicy(VALIDATABLE))
            .serve();

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case LOCK:
                lock(cast(request));
                return null;
            case UNLOCK:
                unlock(cast(request));
                return null;
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}
