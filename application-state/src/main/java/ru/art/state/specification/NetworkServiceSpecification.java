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

package ru.art.state.specification;

import lombok.*;
import ru.art.grpc.server.model.*;
import ru.art.grpc.server.specification.*;
import ru.art.http.server.model.*;
import ru.art.http.server.specification.*;
import ru.art.service.exception.*;
import ru.art.state.api.mapping.*;
import java.util.*;

import static ru.art.core.caster.Caster.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.CollectionMapping.*;
import static ru.art.entity.PrimitiveMapping.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.grpc.server.model.GrpcService.GrpcMethod.*;
import static ru.art.grpc.server.model.GrpcService.*;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.http.server.model.HttpService.*;
import static ru.art.service.constants.RequestValidationPolicy.*;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.Methods.*;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.*;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.PathParameters.*;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.Paths.*;
import static ru.art.state.api.mapping.ClusterProfileRequestResponseMapper.ClusterProfileRequestMapper.*;
import static ru.art.state.api.mapping.ClusterProfileRequestResponseMapper.ClusterProfileResponseMapper.*;
import static ru.art.state.api.mapping.ModuleConnectionRequestMapper.*;
import static ru.art.state.api.mapping.ModuleEndpointMapper.*;
import static ru.art.state.service.NetworkService.*;

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

            .serve(STATE_PATH);

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
