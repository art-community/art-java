/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.grpc.server.function;

import lombok.*;
import io.art.grpc.server.model.*;
import io.art.grpc.server.model.GrpcService.*;
import io.art.grpc.server.specification.*;
import static java.util.Objects.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static io.art.grpc.server.model.GrpcService.*;
import java.util.*;
import java.util.function.*;

@Getter
@RequiredArgsConstructor
public class GrpcFunctionalServiceSpecification implements GrpcServiceSpecification {
    private final String serviceId = GRPC_FUNCTION_SERVICE;
    private final GrpcServiceBuilder grpcServiceBuilder = grpcService();
    private final Map<String, Function<?, ?>> functions = mapOf();

    @Override
    public GrpcService getGrpcService() {
        return grpcServiceBuilder.serve();
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        Function<?, ?> function;
        if (isNull(function = functions.get(methodId))) {
            return null;
        }
        return cast(function.apply(cast(request)));
    }

    void addFunction(String id, GrpcMethod method, Function<?, ?> function) {
        functions.put(id, function);
        grpcServiceBuilder.method(id, method);
    }

}
