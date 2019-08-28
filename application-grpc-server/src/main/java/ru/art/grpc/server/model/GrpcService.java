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

package ru.art.grpc.server.model;

import lombok.*;
import lombok.experimental.*;
import ru.art.entity.*;
import ru.art.entity.interceptor.*;
import ru.art.entity.mapper.*;
import ru.art.service.constants.*;
import static ru.art.grpc.server.module.GrpcServerModule.*;
import java.util.*;

@Getter
@Builder(builderMethodName = "grpcService", buildMethodName = "serve")
public class GrpcService {
    @Singular("method")
    private final Map<String, GrpcMethod> methods;

    @Getter
    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "grpcMethod")
    public static class GrpcMethod {
        private ValueToModelMapper requestMapper;
        private ValueFromModelMapper responseMapper;
        private RequestValidationPolicy validationPolicy;
        private List<ValueInterceptor<Entity, Entity>> requestValueInterceptors = grpcServerModule().getRequestValueInterceptors();
        private List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = grpcServerModule().getResponseValueInterceptors();

        public GrpcMethod addRequestValueInterceptor(ValueInterceptor<Entity, Entity> interceptor) {
            requestValueInterceptors.add(interceptor);
            return this;
        }

        public GrpcMethod addResponseValueInterceptor(ValueInterceptor<Entity, Entity> interceptor) {
            responseValueInterceptors.add(interceptor);
            return this;
        }
    }
}
