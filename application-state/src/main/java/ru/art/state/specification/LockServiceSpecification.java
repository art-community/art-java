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
import static ru.art.core.caster.Caster.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.CollectionMapping.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.grpc.server.model.GrpcService.GrpcMethod.*;
import static ru.art.grpc.server.model.GrpcService.*;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.http.server.model.HttpService.*;
import static ru.art.service.constants.RequestValidationPolicy.*;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.*;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.Methods.*;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.Paths.*;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.Paths.*;
import static ru.art.state.api.mapping.LockRequestMapper.*;
import static ru.art.state.service.LockService.*;
import java.util.*;

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

            .serve(STATE_PATH);

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
