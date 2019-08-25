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

import lombok.*;
import ru.art.grpc.server.model.*;
import ru.art.grpc.server.specification.*;
import java.util.function.*;

import static ru.art.core.caster.Caster.*;

@Getter
@RequiredArgsConstructor
public class GrpcFunctionalServiceSpecification implements GrpcServiceSpecification {
    private final String serviceId;
    private final GrpcService grpcService;
    private final Map<String, Function<?, ?>> functions = mapOf();

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return cast(functions.get(methodId).apply(cast(request)));
    }
}
