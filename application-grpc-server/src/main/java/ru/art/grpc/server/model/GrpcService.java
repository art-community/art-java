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
import lombok.experimental.Accessors;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.service.constants.RequestValidationPolicy;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import java.util.List;
import java.util.Map;

@Getter
@Builder(builderMethodName = "grpcService", buildMethodName = "serve")
public class GrpcService {
    @Singular("method")
    private Map<String, GrpcMethod> methods;

    @Getter
    @Setter
    @Accessors(fluent = true)
    @NoArgsConstructor(staticName = "grpcMethod")
    public static class GrpcMethod {
        private ValueToModelMapper requestMapper;
        private ValueFromModelMapper responseMapper;
        private RequestValidationPolicy validationPolicy;
        private List<ValueInterceptor> requestValueInterceptors = linkedListOf();
        private List<ValueInterceptor> responseValueInterceptors = linkedListOf();

        public GrpcMethod addRequestValueInterceptor(ValueInterceptor interceptor) {
            requestValueInterceptors.add(interceptor);
            return this;
        }

        public GrpcMethod addResponseValueInterceptor(ValueInterceptor interceptor) {
            responseValueInterceptors.add(interceptor);
            return this;
        }
    }
}
