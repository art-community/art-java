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
import io.art.server.service.model.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static java.util.Optional.ofNullable;
import java.util.*;
import java.util.function.*;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceSpecification {
    @EqualsAndHashCode.Include
    private final String id;
    private final Supplier<ServiceConfiguration> configuration;

    @Singular("method")
    private final Map<String, ServiceMethodSpecification> methods;

    public void callBlocking(String methodId) {
        if (deactivated()) {
            return;
        }
        apply(methods.get(methodId), ServiceMethodSpecification::callBlocking);
    }

    public void callBlocking(String methodId, Value requestValue) {
        if (deactivated()) {
            return;
        }
        apply(methods.get(methodId), method -> method.callBlocking(requestValue));
    }


    public Mono<Void> callReactive(String methodId) {
        if (deactivated()) {
            return Mono.empty();
        }
        return let(methods.get(methodId), ServiceMethodSpecification::callReactive);
    }

    public Mono<Void> callReactive(String methodId, Value requestValue) {
        if (deactivated()) {
            return Mono.empty();
        }
        return let(methods.get(methodId), method -> method.callReactive(requestValue));
    }


    public Value executeBlocking(String methodId) {
        if (deactivated()) {
            return null;
        }
        return let(methods.get(methodId), ServiceMethodSpecification::executeBlocking);
    }

    public Value executeBlocking(String methodId, Value requestValue) {
        if (deactivated()) {
            return null;
        }
        return let(methods.get(methodId), method -> method.executeBlocking(requestValue));
    }


    public Mono<Value> executeReactive(String methodId) {
        if (deactivated()) {
            return Mono.empty();
        }
        return let(methods.get(methodId), ServiceMethodSpecification::executeReactive);
    }

    public Mono<Value> executeReactive(String methodId, Value requestValue) {
        if (deactivated()) {
            return Mono.empty();
        }
        return let(methods.get(methodId), method -> method.executeReactive(requestValue));
    }

    public Mono<Value> executeReactive(String methodId, Mono<Value> requestValue) {
        if (deactivated()) {
            return Mono.empty();
        }
        return let(methods.get(methodId), method -> method.executeReactive(requestValue.filter(value -> !configuration.get().isDeactivated())));
    }


    public Flux<Value> stream(String methodId) {
        if (deactivated()) {
            return Flux.empty();
        }
        return let(methods.get(methodId), ServiceMethodSpecification::stream);
    }

    public Flux<Value> stream(String methodId, Value requestValue) {
        if (deactivated()) {
            return Flux.empty();
        }
        return let(methods.get(methodId), method -> method.stream(requestValue));
    }

    public Flux<Value> stream(String methodId, Mono<Value> requestValue) {
        if (deactivated()) {
            return Flux.empty();
        }
        return let(methods.get(methodId), method -> method.stream(requestValue.filter(value -> !configuration.get().isDeactivated())));
    }


    public Flux<Value> channel(String methodId, Flux<Value> requestValue) {
        if (deactivated()) {
            return Flux.empty();
        }
        return let(methods.get(methodId), method -> method.channel(requestValue.filter(value -> !configuration.get().isDeactivated())));
    }


    private boolean deactivated() {
        return ofNullable(configuration)
                .map(Supplier::get)
                .map(ServiceConfiguration::isDeactivated)
                .orElse(false);
    }
}
