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

import io.art.communicator.configuration.*;
import io.art.communicator.implementation.*;
import io.art.core.annotation.*;
import io.art.core.constants.*;
import io.art.core.exception.*;
import io.art.core.lazy.*;
import io.art.core.managed.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.lazy.LazyValue.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;

@Builder
@UsedByGenerator
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunicatorSpecification implements Managed {
    @EqualsAndHashCode.Include
    private final String communicatorId;

    private final MethodProcessingMode inputMode;

    private final MethodProcessingMode outputMode;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    private final ValueFromModelMapper<?, ? extends Value> inputMapper;

    private final ValueToModelMapper<?, ? extends Value> outputMapper;

    private final CommunicatorImplementation implementation;

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Object, Flux<Object>> adoptInput = adoptInput();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Object>, Object> adoptOutput = adoptOutput();

    private final LazyValue<CommunicatorModuleConfiguration> moduleConfiguration = lazy(this::moduleConfiguration);

    private final LazyValue<Optional<CommunicatorConfiguration>> communicatorConfiguration = lazy(this::communicatorConfiguration);

    @Override
    public void initialize() {
        moduleConfiguration.initialize();
        communicatorConfiguration.initialize();
        implementation.initialize();
    }

    @Override
    public void dispose() {
        implementation.dispose();
        communicatorConfiguration.dispose();
        moduleConfiguration.dispose();
    }

    public <T> T communicate() {
        return communicate(null);
    }

    public <T> T communicate(Object input) {
        Scheduler scheduler = communicatorConfiguration.get().map(CommunicatorConfiguration::getScheduler).orElseGet(moduleConfiguration.get()::getScheduler);
        return cast(mapOutput(defer(() -> deferredCommunicate(input)).subscribeOn(scheduler)));
    }

    private Flux<Value> deferredCommunicate(Object input) {
        try {
            Flux<Value> output = implementation.communicate(let(input, this::mapInput, empty()));
            return orElse(output, empty());
        } catch (Throwable throwable) {
            return mapException(throwable);
        }
    }

    private Flux<Value> mapInput(Object input) {
        Flux<Object> inputFlux = getAdoptInput().apply(input).filter(Objects::nonNull);
        for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
            inputFlux = inputFlux.transformDeferred(decorator);
        }
        return cast(inputFlux.map(value -> inputMapper.map(cast(value))));
    }

    private Object mapOutput(Flux<Value> output) {
        Flux<Object> mappedOutput = output.filter(Objects::nonNull).map(value -> outputMapper.map(cast(value)));
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            mappedOutput = mappedOutput.transformDeferred(decorator);
        }
        return getAdoptOutput().apply(mappedOutput);
    }

    private Flux<Value> mapException(Throwable exception) {
        Flux<Object> errorOutput = error(exception);
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            errorOutput = errorOutput.transformDeferred(decorator);
        }
        return cast(errorOutput);
    }

    private Function<Object, Flux<Object>> adoptInput() {
        if (isNull(inputMode)) throw new ImpossibleSituation();
        switch (inputMode) {
            case BLOCKING:
                return Flux::just;
            case MONO:
            case FLUX:
                return input -> from(cast(input));
            default:
                throw new ImpossibleSituation();
        }
    }

    private Function<Flux<Object>, Object> adoptOutput() {
        if (isNull(outputMode)) throw new ImpossibleSituation();
        switch (outputMode) {
            case BLOCKING:
                return Flux::blockFirst;
            case MONO:
                return output -> orNull(output, checking -> checking == Flux.empty(), Flux::last);
            case FLUX:
                return output -> output;
            default:
                throw new ImpossibleSituation();
        }
    }


    private Optional<CommunicatorConfiguration> communicatorConfiguration() {
        return Optional.ofNullable(moduleConfiguration.get().getConfigurations().get(communicatorId));
    }

    private CommunicatorModuleConfiguration moduleConfiguration() {
        return communicatorModule().configuration();
    }
}
