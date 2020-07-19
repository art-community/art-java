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
import io.art.entity.mapper.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.service.implementation.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.server.constants.ServerModuleConstants.ServiceMethodProcessingMode.*;
import static java.util.Optional.*;
import java.util.function.*;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceMethodSpecification {
    @EqualsAndHashCode.Include
    private final String id;
    private final ValueToModelMapper<Object, Value> requestMapper;
    private final ValueFromModelMapper<Object, Value> responseMapper;
    private final ValueFromModelMapper<Throwable, Value> exceptionMapper;
    private final ServiceMethodImplementation implementation;
    private final ServiceMethodProcessingMode requestProcessingMode;
    private final ServiceMethodProcessingMode responseProcessingMode;
    private final Supplier<Boolean> deactivated;

    public void callBlocking() {
        implementation.execute(null);
    }

    public void callBlocking(Value requestValue) {
        Object request = mapRequestBlocking(requestValue);
        implementation.execute(request);
    }


    public Mono<Void> callReactive() {
        try {
            implementation.execute(null);
        } catch (Throwable throwable) {
            if (responseProcessingMode == BLOCKING) {
                throw throwable;
            }
            return Mono.error(throwable);
        }
        return Mono.empty();
    }

    public Mono<Void> callReactive(Value requestValue) {
        try {
            Object request = mapRequestBlocking(requestValue);
            implementation.execute(request);
        } catch (Throwable throwable) {
            if (responseProcessingMode == BLOCKING) {
                throw throwable;
            }
            return Mono.error(throwable);
        }
        return Mono.empty();
    }


    public Value executeBlocking() {
        try {
            Object response = implementation.execute(null);
            return mapResponseBlocking(response);
        } catch (Throwable throwable) {
            return mapExceptionBlocking(throwable);
        }
    }

    public Value executeBlocking(Value requestValue) {
        try {
            Object request = mapRequestBlocking(requestValue);
            Object response = implementation.execute(request);
            return mapResponseBlocking(response);
        } catch (Throwable throwable) {
            return mapExceptionBlocking(throwable);
        }
    }

    public Mono<Value> executeReactive() {
        try {
            Object response = implementation.execute(null);
            return mapResponseReactiveMono(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveMono(throwable);
        }
    }

    public Mono<Value> executeReactive(Value requestValue) {
        try {
            Object request = mapRequestBlocking(requestValue);
            Object response = cast(implementation.execute(request));
            return mapResponseReactiveMono(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveMono(throwable);
        }
    }

    public Mono<Value> executeReactive(Mono<Value> requestValue) {
        try {
            Object request = mapRequestReactive(requestValue);
            Object response = cast(implementation.execute(request));
            return mapResponseReactiveMono(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveMono(throwable);
        }
    }


    public Flux<Value> stream() {
        try {
            Flux<Object> response = cast(implementation.execute(null));
            return mapResponseReactiveFlux(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveFLux(throwable);
        }
    }

    public Flux<Value> stream(Value requestValue) {
        try {
            Object request = mapRequestBlocking(requestValue);
            Flux<Object> response = cast(implementation.execute(request));
            return mapResponseReactiveFlux(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveFLux(throwable);
        }
    }

    public Flux<Value> channel(Flux<Value> requestValue) {
        try {
            Flux<Object> request = mapChannelRequest(requestValue);
            Flux<Object> response = cast(implementation.execute(request));
            return mapChannelResponse(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveFLux(throwable);
        }
    }


    private Object mapRequestBlocking(Value requestValue) {
        switch (requestProcessingMode) {
            case BLOCKING:
                return let(requestValue, requestMapper::map);
            case REACTIVE_MONO:
                return Mono.justOrEmpty(ofNullable(requestValue))
                        .map(requestMapper::map);
            case REACTIVE_FLUX:
                return ofNullable(requestValue)
                        .map(requestMapper::map)
                        .map(Flux::just)
                        .orElse(Flux.empty());
        }
    }

    private Object mapRequestReactive(Mono<Value> requestValue) {
        switch (requestProcessingMode) {
            case BLOCKING:
                return let(requestValue.block(), requestMapper::map);
            case REACTIVE_MONO:
                return requestValue.map(requestMapper::map);
            case REACTIVE_FLUX:
                return Flux.from(requestValue);
        }
    }


    private Value mapResponseBlocking(Object response) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return let(response, responseMapper::map);
            case REACTIVE_MONO:
                return Mono.justOrEmpty(ofNullable(response)).map(responseMapper::map).block();
            case REACTIVE_FLUX:
                return ofNullable(response)
                        .map(responseMapper::map)
                        .map(Flux::just)
                        .orElse(Flux.empty())
                        .blockFirst();
        }
    }

    private Mono<Value> mapResponseReactiveMono(Object response) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return Mono.justOrEmpty(ofNullable(response)).map(responseMapper::map);
            case REACTIVE_MONO:
                return Mono.from(cast(response))
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Mono.just(exceptionMapper.map(throwable)));
            case REACTIVE_FLUX:
                return Flux.from(cast(response)).map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)))
                        .next();
        }
    }

    private Flux<Value> mapResponseReactiveFlux(Object response) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return ofNullable(response)
                        .map(responseMapper::map)
                        .map(Flux::just)
                        .orElse(Flux.empty());
            case REACTIVE_MONO:
            case REACTIVE_FLUX:
                return Flux.from(cast(response))
                        .map(responseMapper::map);
        }
    }


    private Value mapExceptionBlocking(Throwable throwable) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return let(throwable, exceptionMapper::map);
            case REACTIVE_MONO:
                return Mono.justOrEmpty(ofNullable(throwable))
                        .map(exceptionMapper::map)
                        .block();
            case REACTIVE_FLUX:
                return ofNullable(throwable)
                        .map(exceptionMapper::map)
                        .map(Flux::just)
                        .orElse(Flux.empty())
                        .blockFirst();
        }
    }

    private Mono<Value> mapExceptionReactiveMono(Throwable throwable) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return Mono.justOrEmpty(ofNullable(throwable))
                        .map(exceptionMapper::map);
            case REACTIVE_MONO:
            case REACTIVE_FLUX:
                return Mono.just(exceptionMapper.map(throwable));
        }
    }

    private Flux<Value> mapExceptionReactiveFLux(Throwable throwable) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return ofNullable(throwable)
                        .map(exceptionMapper::map)
                        .map(Flux::just)
                        .orElse(Flux.empty());
            case REACTIVE_MONO:
            case REACTIVE_FLUX:
                return Flux.just(exceptionMapper.map(throwable));
        }
    }


    private Flux<Value> mapChannelResponse(Flux<Object> responseChannel) {
        if (responseProcessingMode == REACTIVE_FLUX) {
            return responseChannel.map(responseMapper::map);
        }
    }

    private Flux<Object> mapChannelRequest(Flux<Value> requestChannel) {
        if (requestProcessingMode == REACTIVE_FLUX) {
            return requestChannel.map(requestMapper::map);
        }
    }
}
