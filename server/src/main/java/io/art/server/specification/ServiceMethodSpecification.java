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

import io.art.entity.immutable.Value;
import io.art.entity.mapper.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import io.art.server.implementation.*;
import io.art.server.model.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.*;
import static io.art.server.constants.ServerModuleConstants.ServiceMethodProcessingMode.*;
import static io.art.server.module.ServerModule.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.function.*;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceMethodSpecification {
    @EqualsAndHashCode.Include
    private final String methodId;
    @EqualsAndHashCode.Include
    private final String serviceId;

    @Builder.Default
    private final RequestValidationPolicy validationPolicy = NON_VALIDATABLE;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter(lazy = true)
    private final ServiceSpecification serviceSpecification = specifications().get(serviceId);

    private final ValueToModelMapper<Object, Value> inputMapper;
    private final ValueFromModelMapper<Object, Value> outputMapper;
    private final ValueFromModelMapper<Throwable, Value> exceptionMapper;
    private final ServiceMethodImplementation implementation;
    private final ServiceMethodProcessingMode inputMode;
    private final ServiceMethodProcessingMode outputMode;
    private final ServiceMethodConfiguration configuration;

    public Flux<Value> serve(Flux<Value> input) {
        if (deactivated()) {
            return Flux.empty();
        }
        try {
            Object output = implementation.execute(mapInput(filter(input)));
            if (isNull(output)) {
                return Flux.empty();
            }
            return filter(mapOutput(output));
        } catch (Throwable throwable) {
            return filter(mapException(throwable));
        }
    }

    private Object mapInput(Flux<Value> input) {
        Flux<Object> mappedInput = input
                .map(inputMapper::map)
                .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
        for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
            mappedInput = mappedInput.transformDeferred(decorator);
        }
        switch (inputMode) {
            case BLOCKING:
                return mappedInput.blockFirst();
            case MONO:
                return mappedInput.last();
            case FLUX:
                return mappedInput;
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_REQUEST_TYPE, outputMode), serviceId, methodId);
    }

    private Flux<Value> mapOutput(Object output) {
        if (outputMode == BLOCKING) {
            Flux<Object> mappedOutput = Flux.just(output);
            for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
                mappedOutput = mappedOutput.transformDeferred(decorator);
            }
            return mappedOutput
                    .map(outputMapper::map)
                    .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
        }

        if (outputMode == MONO || outputMode == FLUX) {
            Flux<Object> mappedOutput = Flux.from(cast(output));
            for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
                mappedOutput = mappedOutput.transformDeferred(decorator);
            }
            return mappedOutput
                    .map(outputMapper::map)
                    .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
        }

        throw new ServiceMethodExecutionException(format(UNKNOWN_RESPONSE_TYPE, outputMode), serviceId, methodId);
    }

    private Flux<Value> mapException(Throwable exception) {
        Flux<Object> errorOutput = Flux.error(exception);
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            errorOutput = errorOutput.transformDeferred(decorator);
        }
        return errorOutput
                .map(outputMapper::map)
                .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));

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
