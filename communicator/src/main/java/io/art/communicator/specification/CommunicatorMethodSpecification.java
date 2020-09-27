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

package io.art.communicator.specification;

import com.google.common.collect.*;
import io.art.communicator.configuration.*;
import io.art.communicator.implementation.*;
import io.art.communicator.interceptor.*;
import io.art.entity.immutable.Value;
import io.art.entity.mapper.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static java.util.Optional.*;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunicatorMethodSpecification {
    @EqualsAndHashCode.Include
    private final String operationId;

    @Singular("interceptor")
    private final ImmutableList<CommunicatorInterceptor<Object, Object>> interceptors;

    private final ValueFromModelMapper<Object, Value> requestMapper;
    private final ValueToModelMapper<Object, Value> responseMapper;
    private final CommunicatorImplementation implementation;
    private final CommunicatorConfiguration configuration;

    public void callBlocking() {
        if (deactivated()) {
            return;
        }
        implementation.callBlocking();
    }

    public void callBlocking(Object request) {
        if (deactivated()) {
            return;
        }
        implementation.callBlocking(mapRequestBlocking(request));
    }


    public Mono<Void> callReactive() {
        if (deactivated()) {
            return Mono.empty();
        }
        return implementation.callReactive();
    }

    public Mono<Void> callReactive(Object request) {
        if (deactivated()) {
            return Mono.empty();
        }
        return implementation.callReactive(mapRequestBlocking(request));
    }


    public <T> T executeBlocking() {
        if (deactivated()) {
            return null;
        }
        return cast(mapResponseBlocking(implementation.executeBlocking()));
    }

    public <T> T executeBlocking(Object request) {
        if (deactivated()) {
            return null;
        }
        return cast(mapResponseBlocking(implementation.executeBlocking(mapRequestBlocking(request))));
    }

    public <T> Mono<T> executeReactive() {
        if (deactivated()) {
            return Mono.empty();
        }
        return mapResponseReactiveMono(implementation.executeReactive());
    }

    public <T> Mono<T> executeReactive(Object requestValue) {
        if (deactivated()) {
            return Mono.empty();
        }
        return mapResponseReactiveMono(implementation.executeReactive(mapRequestBlocking(requestValue)));
    }

    public <T> Mono<T> executeReactive(Mono<Object> requestValue) {
        if (deactivated()) {
            return Mono.empty();
        }
        return mapResponseReactiveMono(implementation.executeReactive(mapRequestReactive(requestValue)));
    }


    public <T> Flux<T> stream() {
        if (deactivated()) {
            return Flux.empty();
        }
        return mapResponseReactiveFlux(implementation.stream());
    }

    public <T> Flux<T> stream(Object request) {
        if (deactivated()) {
            return Flux.empty();
        }
        return mapResponseReactiveFlux(implementation.stream(mapRequestBlocking(request)));
    }

    public <T> Flux<T> stream(Mono<Object> requestValue) {
        if (deactivated()) {
            return Flux.empty();
        }
        return mapResponseReactiveFlux(implementation.stream(mapRequestReactive(requestValue.filter(value -> !deactivated()))));
    }

    public <T> Flux<T> channel(Flux<Object> requestValue) {
        if (deactivated()) {
            return Flux.empty();
        }
        return mapChannelResponse(implementation.channel(mapChannelRequest(requestValue.filter(value -> !deactivated()))));
    }


    private Value mapRequestBlocking(Object request) {
        return requestMapper.map(request);
    }

    private Mono<Value> mapRequestReactive(Mono<Object> requestValue) {
        return requestValue.map(requestMapper::map);

    }

    private <T> T mapResponseBlocking(Value responseValue) {
        return cast(responseMapper.map(responseValue));
    }

    private <T> Mono<T> mapResponseReactiveMono(Mono<Value> response) {
        return response.map(data -> cast(responseMapper.map(data)));
    }

    private <T> Flux<T> mapResponseReactiveFlux(Flux<Value> response) {
        return response.map(data -> cast(responseMapper.map(data)));
    }

    private Flux<Value> mapChannelRequest(Flux<Object> requestChannel) {
        return requestChannel.map(requestMapper::map);

    }

    private <T> Flux<T> mapChannelResponse(Flux<Value> responseChannel) {
        return responseChannel.map(response -> cast(responseMapper.map(response)));
    }

    private boolean deactivated() {
        return ofNullable(getConfiguration())
                .map(CommunicatorConfiguration::isDeactivated)
                .orElse(false);
    }
}
