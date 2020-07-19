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

package io.art.server.service.specification;

import io.art.entity.immutable.Value;
import io.art.server.service.configuration.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import java.util.*;
import java.util.function.*;

@Getter
@Builder
public class ServiceSpecification<C extends ServiceConfiguration> {
    private final String type;
    private final String serviceId;
    private final C configuration;
    @Singular("method")
    private final Map<String, ServiceMethodSpecification<?>> methods;
    private final Supplier<Boolean> deactivated;

    public void callBlocking(String methodId) {
        apply(methods.get(methodId), ServiceMethodSpecification::callBlocking);
    }

    public void callBlocking(String methodId, Value requestValue) {
        apply(methods.get(methodId), method -> method.callBlocking(requestValue));
    }

    public Mono<Void> callReactive(String methodId) {
        return let(methods.get(methodId), ServiceMethodSpecification::callReactive);
    }

    public Mono<Void> callReactive(String methodId, Value requestValue) {
        return let(methods.get(methodId), method -> method.callReactive(requestValue));
    }

    public Value executeBlocking(String methodId) {
        return let(methods.get(methodId), ServiceMethodSpecification::executeBlocking);
    }

    public Value executeBlocking(String methodId, Value requestValue) {
        return let(methods.get(methodId), method -> method.executeBlocking(requestValue));
    }

    public Mono<Value> executeReactive(String methodId) {
        return let(methods.get(methodId), ServiceMethodSpecification::executeReactive);
    }

    public Mono<Value> executeReactive(String methodId, Value requestValue) {
        return let(methods.get(methodId), method -> method.executeReactive(requestValue));
    }

    public Mono<Value> executeReactive(String methodId, Mono<Value> requestValue) {
        return let(methods.get(methodId), method -> method.executeReactive(requestValue));
    }

    public Flux<Value> stream(String methodId) {
        return let(methods.get(methodId), ServiceMethodSpecification::stream);
    }

    public Flux<Value> stream(String methodId, Value requestValue) {
        return let(methods.get(methodId), method -> method.stream(requestValue));
    }

    public Flux<Value> channel(String methodId, Flux<Value> requestValue) {
        return let(methods.get(methodId), method -> method.channel(requestValue));
    }
}
