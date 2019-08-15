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

package ru.art.grpc.server.function;

import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.grpc.server.model.GrpcService.GrpcMethod;
import ru.art.service.constants.RequestValidationPolicy;
import static ru.art.core.caster.Caster.cast;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.EXECUTE_GRPC_FUNCTION;
import static ru.art.grpc.server.model.GrpcService.*;
import static ru.art.service.ServiceModule.serviceModule;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GrpcServiceFunction {
    private GrpcMethod grpcMethod = GrpcMethod.grpcMethod();
    private final String serviceId;

    private GrpcServiceFunction(String serviceId) {
        this.serviceId = serviceId;
    }

    public <ResponseType> GrpcServiceFunction responseMapper(ValueFromModelMapper<ResponseType, ? extends Value> responseMapper) {
        grpcMethod.responseMapper(responseMapper);
        return this;
    }

    public <RequestType> GrpcServiceFunction requestMapper(ValueToModelMapper<RequestType, ? extends Value> requestMapper) {
        grpcMethod.requestMapper(requestMapper);
        return this;
    }

    public GrpcServiceFunction validationPolicy(RequestValidationPolicy policy) {
        grpcMethod.validationPolicy(policy);
        return this;
    }

    public GrpcServiceFunction addRequestValueInterceptor(ValueInterceptor interceptor) {
        grpcMethod.addRequestValueInterceptor(interceptor);
        return this;
    }

    public GrpcServiceFunction addResponseValueInterceptor(ValueInterceptor interceptor) {
        grpcMethod.addResponseValueInterceptor(interceptor);
        return this;
    }

    public <RequestType, ResponseType> void handle(Function<RequestType, ResponseType> function) {
        serviceModule()
                .getServiceRegistry()
                .registerService(new GrpcFunctionalServiceSpecification(serviceId, grpcService()
                        .method(EXECUTE_GRPC_FUNCTION, grpcMethod)
                        .serve(), function));
    }

    public <RequestType> void consume(Consumer<RequestType> consumer) {
        handle(request -> {
            consumer.accept(cast(request));
            return null;
        });
    }

    public <ResponseType> void produce(Supplier<ResponseType> producer) {
        handle(request -> producer.get());
    }

    public static GrpcServiceFunction grpc(String serviceId) {
        return new GrpcServiceFunction(serviceId);
    }
}