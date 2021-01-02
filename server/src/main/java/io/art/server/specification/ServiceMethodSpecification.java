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

import io.art.core.annotation.*;
import io.art.core.caster.*;
import io.art.core.constants.*;
import io.art.core.model.*;
import io.art.server.configuration.*;
import io.art.server.exception.*;
import io.art.server.implementation.*;
import io.art.server.model.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.model.ServiceMethodIdentifier.serviceMethod;
import static io.art.server.constants.ServerModuleConstants.ExceptionMessages.*;
import static io.art.server.module.ServerModule.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
@UsedByGenerator
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceMethodSpecification {
    @Builder.Default
    private final ValueToModelMapper<?, ? extends Value> inputMapper = Caster::cast;
    @Builder.Default
    private final ValueFromModelMapper<?, ? extends Value> outputMapper = Caster::cast;
    private final ValueFromModelMapper<Throwable, Value> exceptionMapper;
    private final ServiceMethodImplementation implementation;
    private final MethodProcessingMode inputMode;
    private final MethodProcessingMode outputMode;

    @EqualsAndHashCode.Include
    private final String methodId;

    @EqualsAndHashCode.Include
    private final String serviceId;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Singular("exceptionDecorator")
    private final List<UnaryOperator<Flux<Object>>> exceptionDecorator;

    @Getter(lazy = true)
    private final ServerModuleConfiguration moduleConfiguration = serverModule().configuration();

    @Getter(lazy = true)
    private final ServiceSpecification serviceSpecification = specifications().get(serviceId).orElseThrow(IllegalStateException::new);

    @Getter(lazy = true)
    private final ServiceConfiguration serviceConfiguration = getServiceSpecification().getConfiguration();

    @Getter(lazy = true)
    private final ServiceMethodConfiguration methodConfiguration = let(getServiceConfiguration(), configuration -> configuration.getMethods().get(methodId));

    public Flux<Value> serve(Flux<Value> input) {
        if (deactivated()) {
            return Flux.empty();
        }
        Scheduler scheduler = let(getMethodConfiguration(), ServiceMethodConfiguration::getScheduler, getModuleConfiguration().getScheduler());
        return Flux.defer(() -> deferredServe(input)).subscribeOn(scheduler);
    }

    private Flux<Value> deferredServe(Flux<Value> input) {
        try {
            Object output = implementation.execute(mapInput(input));
            if (isNull(output)) return Flux.empty();
            return mapOutput(output);
        } catch (Throwable throwable) {
            return mapException(throwable);
        }
    }

    private Object mapInput(Flux<Value> input) {
        Flux<Object> mappedInput = input.filter(value -> !deactivated()).map(value -> inputMapper.map(cast(value)));
        for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
            mappedInput = mappedInput.transformDeferred(decorator);
        }
        mappedInput = mappedInput.onErrorResume(Throwable.class, this::mapException);
        switch (inputMode) {
            case BLOCKING:
                return mappedInput.blockFirst();
            case MONO:
                return mappedInput.last();
            case FLUX:
                return mappedInput;
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_INPUT_MODE, inputMode), serviceMethod(serviceId, methodId));
    }

    private Flux<Value> mapOutput(Object output) {
        Flux<Object> mappedOutput;
        switch (outputMode) {
            case BLOCKING:
                mappedOutput = Flux.just(output);
                break;
            case MONO:
            case FLUX:
                mappedOutput = Flux.from(cast(output));
                break;
            default:
                throw new ServiceMethodExecutionException(format(UNKNOWN_OUTPUT_MODE, outputMode), serviceMethod(serviceId, methodId));
        }
        mappedOutput = mappedOutput.filter(Objects::nonNull).filter(value -> !deactivated());
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            mappedOutput = mappedOutput.transformDeferred(decorator);
        }
        return mappedOutput
                .map(value -> (Value) outputMapper.map(cast(value)))
                .onErrorResume(Throwable.class, this::mapException);
    }

    private Flux<Value> mapException(Throwable exception) {
        Flux<Object> errorOutput = Flux.error(exception).filter(value -> !deactivated());
        for (UnaryOperator<Flux<Object>> decorator : exceptionDecorator) {
            errorOutput = errorOutput.transformDeferred(decorator);
        }
        return errorOutput
                .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)))
                .cast(Value.class);

    }

    private boolean deactivated() {
        ServiceConfiguration serviceConfiguration = getServiceConfiguration();
        if (isNull(serviceConfiguration)) {
            return false;
        }
        if (serviceConfiguration.isDeactivated()) {
            return true;
        }
        if (isNull(getMethodConfiguration())) {
            return false;
        }
        return getMethodConfiguration().isDeactivated();
    }
}
