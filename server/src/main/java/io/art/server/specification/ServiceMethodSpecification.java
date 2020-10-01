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

package io.art.server.specification;

import com.google.common.collect.*;
import io.art.entity.immutable.Value;
import io.art.entity.mapper.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import io.art.server.implementation.*;
import io.art.server.interceptor.*;
import io.art.server.model.*;
import lombok.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.*;
import static io.art.server.constants.ServerModuleConstants.ServiceMethodPayloadType.*;
import static io.art.server.module.ServerModule.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.isNull;
import static java.util.Optional.*;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceMethodSpecification {
    @EqualsAndHashCode.Include
    private final String methodId;
    @EqualsAndHashCode.Include
    private final String serviceId;

    @Getter(lazy = true)
    private final ServiceSpecification serviceSpecification = specifications().get(serviceId);

    @Builder.Default
    private final RequestValidationPolicy validationPolicy = NON_VALIDATABLE;

    @Singular("interceptor")
    private final ImmutableList<ServiceMethodInterceptor<Object, Object>> interceptors;

    private final ValueToModelMapper<Object, Value> requestMapper;
    private final ValueFromModelMapper<Object, Value> responseMapper;
    private final ValueFromModelMapper<Throwable, Value> exceptionMapper;
    private final ServiceMethodImplementation implementation;
    private final ServiceMethodPayloadType requestType;
    private final ServiceMethodPayloadType responseType;
    private final ServiceMethodConfiguration configuration;

    public Flux<Value> stream(Value requestValue) {
        if (deactivated()) {
            return Flux.empty();
        }
        try {
            Object request = let(requestValue, this::mapStreamRequest);
            Object response = cast(implementation.execute(request));
            if (isNull(response)) {
                return Flux.empty();
            }
            return mapResponse(response);
        } catch (Throwable throwable) {
            return Flux.just(exceptionMapper.map(throwable));
        }
    }

    public Flux<Value> channel(Publisher<Value> input) {
        if (deactivated()) {
            return Flux.empty();
        }
        try {
            Flux<Object> request = let(input, requestValue -> mapChannelRequest(Flux.from(requestValue).filter(value -> !deactivated())), Flux.empty());
            Object response = cast(implementation.execute(request));
            if (isNull(response)) {
                return Flux.empty();
            }
            return mapResponse(response);
        } catch (Throwable throwable) {
            return Flux.just(exceptionMapper.map(throwable));
        }
    }


    private Object mapStreamRequest(Value requestValue) {
        switch (requestType) {
            case VALUE:
                return requestMapper.map(requestValue);
            case MONO:
                return ofNullable(requestValue)
                        .map(value -> Mono
                                .just(requestMapper.map(requestValue))
                                .onErrorResume(Throwable.class, throwable -> Mono.just(exceptionMapper.map(throwable))))
                        .orElseGet(Mono::empty);
            case FLUX:
                return ofNullable(requestValue)
                        .map(value -> Flux
                                .just(requestMapper.map(requestValue))
                                .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable))))
                        .orElseGet(Flux::empty);
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseType), serviceId, methodId);
    }

    private Flux<Object> mapChannelRequest(Flux<Value> requestChannel) {
        if (requestType == FLUX) {
            return requestChannel.map(requestMapper::map);
        }
        throw new ServiceMethodExecutionException(format(INVALID_CHANNEL_PROCESSING_MODE, requestType), serviceId, methodId);
    }

    private Flux<Value> mapResponse(Object response) {
        switch (responseType) {
            case VALUE:
                return Flux
                        .just(responseMapper.map(response))
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
            case MONO:
            case FLUX:
                return Flux
                        .from(cast(response))
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseType), serviceId, methodId);
    }


    private boolean deactivated() {
        boolean serviceDeactivated = ofNullable(getServiceSpecification().getConfiguration())
                .map(ServiceConfiguration::isDeactivated)
                .orElse(false);
        Boolean methodDeactivated = ofNullable(configuration)
                .map(ServiceMethodConfiguration::isDeactivated)
                .orElse(false);
        return serviceDeactivated || methodDeactivated;
    }
}
