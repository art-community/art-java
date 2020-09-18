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

import com.google.common.collect.*;
import io.art.entity.immutable.Value;
import io.art.entity.mapper.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import io.art.server.interceptor.*;
import io.art.server.service.implementation.*;
import io.art.server.service.model.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.*;
import static io.art.server.constants.ServerModuleConstants.ServiceMethodProcessingMode.*;
import static java.text.MessageFormat.*;
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
    private final Supplier<ServiceMethodConfiguration> configuration;
    private final ServiceSpecification serviceSpecification;
    @Builder.Default
    private final RequestValidationPolicy validationPolicy = NON_VALIDATABLE;
    @Singular("interceptor")
    private final ImmutableList<ServiceExecutionInterceptor<Object, Object>> interceptors;

    public void callBlocking() {
        if (isDeactivated()) {
            return;
        }
        implementation.execute();
    }

    public void callBlocking(Value requestValue) {
        if (isDeactivated()) {
            return;
        }
        Object request = mapRequestBlocking(requestValue);
        implementation.execute(request);
    }


    public Mono<Void> callReactive() {
        if (isDeactivated()) {
            return Mono.empty();
        }
        try {
            implementation.execute();
        } catch (Throwable throwable) {
            if (responseProcessingMode == BLOCKING) {
                throw throwable;
            }
            return Mono.error(throwable);
        }
        return Mono.empty();
    }

    public Mono<Void> callReactive(Value requestValue) {
        if (isDeactivated()) {
            return Mono.empty();
        }
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
        if (isDeactivated()) {
            return null;
        }
        try {
            Object response = implementation.execute();
            return mapResponseBlocking(response);
        } catch (Throwable throwable) {
            return mapExceptionBlocking(throwable);
        }
    }

    public Value executeBlocking(Value requestValue) {
        if (isDeactivated()) {
            return null;
        }
        try {
            Object request = mapRequestBlocking(requestValue);
            Object response = implementation.execute(request);
            return mapResponseBlocking(response);
        } catch (Throwable throwable) {
            return mapExceptionBlocking(throwable);
        }
    }

    public Mono<Value> executeReactive() {
        if (isDeactivated()) {
            return Mono.empty();
        }
        try {
            Object response = implementation.execute();
            return mapResponseReactiveMono(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveMono(throwable);
        }
    }

    public Mono<Value> executeReactive(Value requestValue) {
        if (isDeactivated()) {
            return Mono.empty();
        }
        try {
            return Mono.create(emitter -> {
                Object request = mapRequestBlocking(requestValue);
                Object response = cast(implementation.execute(request));
                emitter.success(mapResponseBlocking(response));
            });
        } catch (Throwable throwable) {
            return mapExceptionReactiveMono(throwable);
        }
    }

    public Mono<Value> executeReactive(Mono<Value> requestValue) {
        if (isDeactivated()) {
            return Mono.empty();
        }
        try {
            Object request = mapRequestReactive(requestValue.filter(value -> !isDeactivated()));
            Object response = cast(implementation.execute(request));
            return mapResponseReactiveMono(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveMono(throwable);
        }
    }


    public Flux<Value> stream() {
        if (isDeactivated()) {
            return Flux.empty();
        }
        try {
            Flux<Object> response = cast(implementation.execute());
            return mapResponseReactiveFlux(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveFLux(throwable);
        }
    }

    public Flux<Value> stream(Value requestValue) {
        if (isDeactivated()) {
            return Flux.empty();
        }
        try {
            Object request = mapRequestBlocking(requestValue);
            Flux<Object> response = cast(implementation.execute(request));
            return mapResponseReactiveFlux(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveFLux(throwable);
        }
    }

    public Flux<Value> stream(Mono<Value> requestValue) {
        if (isDeactivated()) {
            return Flux.empty();
        }
        try {
            Object request = mapRequestReactive(requestValue.filter(value -> !isDeactivated()));
            Flux<Object> response = cast(implementation.execute(request));
            return mapResponseReactiveFlux(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveFLux(throwable);
        }
    }

    public Flux<Value> channel(Flux<Value> requestValue) {
        if (isDeactivated()) {
            return Flux.empty();
        }
        try {
            Flux<Object> request = mapChannelRequest(requestValue.filter(value -> !isDeactivated()));
            Flux<Object> response = cast(implementation.execute(request));
            return mapChannelResponse(response);
        } catch (Throwable throwable) {
            return mapExceptionReactiveFLux(throwable);
        }
    }


    private Object mapRequestBlocking(Value requestValue) {
        switch (requestProcessingMode) {
            case BLOCKING:
                return requestMapper.map(requestValue);
            case REACTIVE_MONO:
                return Mono.just(requestMapper.map(requestValue));
            case REACTIVE_FLUX:
                return Flux.just(requestMapper.map(requestValue));
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseProcessingMode));
    }

    private Object mapRequestReactive(Mono<Value> requestValue) {
        switch (requestProcessingMode) {
            case BLOCKING:
                return requestValue.map(requestMapper::map).block();
            case REACTIVE_MONO:
                return requestValue.map(requestMapper::map);
            case REACTIVE_FLUX:
                return Flux.from(requestValue).map(requestMapper::map).next();
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseProcessingMode));
    }


    private Value mapResponseBlocking(Object response) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return let(response, responseMapper::map);
            case REACTIVE_MONO:
                return Mono.justOrEmpty(ofNullable(response))
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Mono.just(exceptionMapper.map(throwable)))
                        .block();
            case REACTIVE_FLUX:
                return ofNullable(response)
                        .map(responseMapper::map)
                        .map(Flux::just)
                        .orElse(Flux.empty())
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)))
                        .blockFirst();
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseProcessingMode));
    }

    private Mono<Value> mapResponseReactiveMono(Object response) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return Mono.justOrEmpty(ofNullable(response))
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Mono.just(exceptionMapper.map(throwable)));
            case REACTIVE_MONO:
                return Mono.from(cast(response))
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Mono.just(exceptionMapper.map(throwable)));
            case REACTIVE_FLUX:
                return Flux.from(cast(response))
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)))
                        .next();
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseProcessingMode));
    }

    private Flux<Value> mapResponseReactiveFlux(Object response) {
        switch (responseProcessingMode) {
            case BLOCKING:
                return ofNullable(response)
                        .map(responseMapper::map)
                        .map(Flux::just)
                        .orElse(Flux.empty())
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
            case REACTIVE_MONO:
            case REACTIVE_FLUX:
                return Flux.from(cast(response))
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseProcessingMode));
    }


    private Value mapExceptionBlocking(Throwable throwable) {
        return exceptionMapper.map(throwable);
    }

    private Mono<Value> mapExceptionReactiveMono(Throwable throwable) {
        return Mono.just(exceptionMapper.map(throwable));
    }

    private Flux<Value> mapExceptionReactiveFLux(Throwable throwable) {
        return Flux.just(exceptionMapper.map(throwable));
    }


    private Flux<Object> mapChannelRequest(Flux<Value> requestChannel) {
        if (requestProcessingMode == REACTIVE_FLUX) {
            return requestChannel.map(requestMapper::map);
        }
        throw new ServiceMethodExecutionException(format(INVALID_CHANNEL_PROCESSING_MODE, requestProcessingMode));
    }

    private Flux<Value> mapChannelResponse(Flux<Object> responseChannel) {
        if (responseProcessingMode == REACTIVE_FLUX) {
            return responseChannel
                    .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)))
                    .map(responseMapper::map);
        }
        throw new ServiceMethodExecutionException(format(INVALID_CHANNEL_PROCESSING_MODE, responseProcessingMode));
    }

    private boolean isDeactivated() {
        return ofNullable(configuration)
                .map(Supplier::get)
                .map(ServiceMethodConfiguration::isDeactivated)
                .orElse(false);
    }
}
