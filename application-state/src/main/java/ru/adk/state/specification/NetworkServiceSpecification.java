package ru.adk.state.specification;

import lombok.Getter;
import ru.adk.grpc.server.model.GrpcService;
import ru.adk.grpc.server.specification.GrpcServiceSpecification;
import ru.adk.http.server.model.HttpService;
import ru.adk.http.server.specification.HttpServiceSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import ru.adk.state.api.mapping.NetworkServicePathParametersMapper;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.entity.CollectionMapping.stringCollectionMapper;
import static ru.adk.entity.PrimitiveMapping.intMapper;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.adk.grpc.server.model.GrpcService.GrpcMethod.grpcMethod;
import static ru.adk.grpc.server.model.GrpcService.grpcService;
import static ru.adk.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.adk.http.server.model.HttpService.httpService;
import static ru.adk.http.server.module.HttpServerModule.httpServerModule;
import static ru.adk.service.constants.RequestValidationPolicy.VALIDATABLE;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.Methods.*;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.NETWORK_SERVICE_ID;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.PathParameters.PROFILE;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.Paths.*;
import static ru.adk.state.api.mapping.ClusterProfileRequestResponseMapper.ClusterProfileRequestMapper.toClusterProfileRequest;
import static ru.adk.state.api.mapping.ClusterProfileRequestResponseMapper.ClusterProfileResponseMapper.fromClusterProfileResponse;
import static ru.adk.state.api.mapping.ModuleConnectionRequestMapper.toModuleConnectionRequest;
import static ru.adk.state.api.mapping.ModuleEndpointMapper.toModuleEndpoint;
import static ru.adk.state.service.NetworkService.*;
import java.util.List;

@Getter
public class NetworkServiceSpecification implements HttpServiceSpecification, GrpcServiceSpecification {
    private final List<String> serviceTypes = fixedArrayOf(HTTP_SERVICE_TYPE, GRPC_SERVICE_TYPE);
    private final String serviceId = NETWORK_SERVICE_ID;
    private final HttpService httpService = httpService()
            .get(GET_CLUSTER_PROFILE)
            .consumes(applicationJsonUtf8())
            .fromPathParameters(PROFILE)
            .validationPolicy(VALIDATABLE)
            .requestMapper(NetworkServicePathParametersMapper.toClusterProfileRequest)
            .produces(applicationJsonUtf8())
            .responseMapper(fromClusterProfileResponse)
            .listen(GET_CLUSTER_PROFILE_PATH)

            .post(CONNECT)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(toModuleConnectionRequest)
            .produces(applicationJsonUtf8())
            .listen(CONNECT_PATH)

            .post(INCREMENT_SESSION)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(toModuleEndpoint)
            .listen(INCREMENT_SESSION_PATH)

            .post(DECREMENT_SESSION)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(toModuleEndpoint)
            .listen(DECREMENT_SESSION_PATH)

            .get(GET_SESSIONS)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(toModuleEndpoint)
            .produces(applicationJsonUtf8())
            .responseMapper(intMapper.getFromModel())
            .listen(GET_SESSIONS_PATH)

            .get(GET_PROFILES)
            .produces(applicationJsonUtf8())
            .responseMapper(stringCollectionMapper.getFromModel())
            .listen(GET_PROFILES_PATH)

            .serve(httpServerModule().getPath());

    private final GrpcService grpcService = grpcService()
            .method(GET_CLUSTER_PROFILE, grpcMethod()
                    .requestMapper(toClusterProfileRequest)
                    .responseMapper(fromClusterProfileResponse)
                    .validationPolicy(VALIDATABLE))
            .method(CONNECT, grpcMethod()
                    .requestMapper(toModuleConnectionRequest)
                    .validationPolicy(VALIDATABLE))
            .method(INCREMENT_SESSION, grpcMethod()
                    .requestMapper(toModuleConnectionRequest)
                    .validationPolicy(VALIDATABLE))
            .method(DECREMENT_SESSION, grpcMethod()
                    .requestMapper(toModuleConnectionRequest)
                    .validationPolicy(VALIDATABLE))
            .method(GET_SESSIONS, grpcMethod()
                    .requestMapper(toModuleConnectionRequest)
                    .responseMapper(intMapper.getFromModel())
                    .validationPolicy(VALIDATABLE))
            .method(GET_PROFILES, grpcMethod().responseMapper(stringCollectionMapper.getFromModel()))
            .serve();

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case GET_CLUSTER_PROFILE:
                return cast(getClusterProfile(cast(request)));
            case CONNECT:
                connect(cast(request));
                return null;
            case INCREMENT_SESSION:
                incrementSession(cast(request));
                return null;
            case DECREMENT_SESSION:
                decrementSession(cast(request));
                return null;
            case GET_SESSIONS:
                return cast(getSessions(cast(request)));
            case GET_PROFILES:
                return cast(getProfiles());
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}
