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
import io.art.core.constants.*;
import io.art.core.exception.*;
import io.art.server.configuration.*;
import io.art.server.implementation.*;
import io.art.server.model.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import io.art.value.mapping.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.server.module.ServerModule.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;

@Builder(toBuilder = true)
@UsedByGenerator
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceMethodSpecification {
    @Getter
    @EqualsAndHashCode.Include
    private final String methodId;

    @Getter
    @EqualsAndHashCode.Include
    private final String serviceId;

    @Getter
    private final MethodProcessingMode inputMode;

    @Getter
    private final MethodProcessingMode outputMode;

    private final ValueToModelMapper<?, ? extends Value> inputMapper;
    private final ValueFromModelMapper<?, ? extends Value> outputMapper;

    @Builder.Default
    private final ValueFromModelMapper<Throwable, ? extends Value> exceptionMapper = ThrowableMapping::fromThrowable;

    private final ServiceMethodImplementation implementation;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter(lazy = true, value = PRIVATE)
    private final ServerModuleConfiguration moduleConfiguration = serverModule().configuration();

    @Getter(lazy = true, value = PRIVATE)
    private final ServiceSpecification serviceSpecification = specifications().get(serviceId).orElseThrow(ImpossibleSituation::new);

    @Getter(lazy = true, value = PRIVATE)
    private final ServiceConfiguration serviceConfiguration = getServiceSpecification().getConfiguration();

    @Getter(lazy = true, value = PRIVATE)
    private final ServiceMethodConfiguration methodConfiguration = let(getServiceConfiguration(), configuration -> configuration.getMethods().get(methodId));

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Value>, Object> mapInput = selectMapInput();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Object, Flux<Object>> mapOutput = selectMapOutput();

    public Flux<Value> serve(Flux<Value> input) {
        if (deactivated()) {
            return Flux.empty();
        }
        Scheduler scheduler = let(getMethodConfiguration(), ServiceMethodConfiguration::getScheduler, getModuleConfiguration().getScheduler());
        return defer(() -> deferredServe(input)).subscribeOn(scheduler);
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
        return getMapInput().apply(cast(mappedInput.onErrorResume(Throwable.class, this::mapException)));
    }

    private Flux<Value> mapOutput(Object output) {
        Flux<Object> mappedOutput = getMapOutput().apply(output);
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
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
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

    private Function<Flux<Value>, Object> selectMapInput() {
        if (isNull(inputMode)) {
            throw new ImpossibleSituation();
        }
        switch (inputMode) {
            case BLOCKING:
                return Flux::blockFirst;
            case MONO:
                return mappedInput -> orNull(mappedInput, checking -> checking == Flux.<Value>empty(), Flux::last);
            case FLUX:
                return mappedInput -> mappedInput;
            default:
                throw new ImpossibleSituation();
        }
    }

    private Function<Object, Flux<Object>> selectMapOutput() {
        if (isNull(outputMode)) {
            throw new ImpossibleSituation();
        }
        switch (outputMode) {
            case BLOCKING:
                return Flux::just;
            case MONO:
            case FLUX:
                return output -> Flux.from(cast(output));
            default:
                throw new ImpossibleSituation();
        }
    }
}
