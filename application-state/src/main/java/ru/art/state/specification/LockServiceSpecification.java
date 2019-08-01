package ru.art.state.specification;

import lombok.Getter;
import ru.art.grpc.server.model.GrpcService;
import ru.art.grpc.server.specification.GrpcServiceSpecification;
import ru.art.http.server.model.HttpService;
import ru.art.http.server.specification.HttpServiceSpecification;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.entity.CollectionMapping.stringCollectionMapper;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.art.grpc.server.model.GrpcService.GrpcMethod.grpcMethod;
import static ru.art.grpc.server.model.GrpcService.grpcService;
import static ru.art.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.art.http.server.model.HttpService.httpService;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.service.constants.RequestValidationPolicy.VALIDATABLE;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.LOCK_SERVICE_ID;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.Methods.*;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.Paths.*;
import static ru.art.state.api.mapping.LockRequestMapper.toLockRequest;
import static ru.art.state.service.LockService.lock;
import static ru.art.state.service.LockService.unlock;
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
