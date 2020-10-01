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
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.*;
import static io.art.server.module.ServerModule.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;

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

    public Flux<Value> serve(Flux<Value> input) {
        if (deactivated()) {
            return Flux.empty();
        }
        try {
            Object request = mapRequest(filter(input));
            Object response = cast(implementation.execute(request));
            if (isNull(response)) {
                return Flux.empty();
            }
            return mapResponse(response);
        } catch (Throwable throwable) {
            return Flux.just(exceptionMapper.map(throwable));
        }
    }

    private Object mapRequest(Flux<Value> requestChannel) {
        switch (requestType) {
            case VALUE:
                return requestChannel.blockFirst();
            case MONO:
                return requestChannel
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)))
                        .last();
            case FLUX:
                return requestChannel
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseType), serviceId, methodId);
    }

    private Flux<Value> mapResponse(Object response) {
        switch (responseType) {
            case VALUE:
                return Flux
                        .just(responseMapper.map(response))
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
            case MONO:
            case FLUX:
                return filter(Flux.from(cast(response)))
                        .map(responseMapper::map)
                        .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_PROCESSING_MODE, responseType), serviceId, methodId);
    }


    private Flux<Value> filter(Flux<Value> input) {
        return input.filter(Objects::nonNull).filter(value -> !deactivated());
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
