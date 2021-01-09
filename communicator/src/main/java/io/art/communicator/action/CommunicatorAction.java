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

package io.art.communicator.action;

import io.art.communicator.configuration.*;
import io.art.communicator.implementation.*;
import io.art.core.annotation.*;
import io.art.core.constants.*;
import io.art.core.exception.*;
import io.art.core.lazy.*;
import io.art.core.managed.*;
import io.art.core.model.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.lazy.ManagedValue.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.empty;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;

@Builder
@UsedByGenerator
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunicatorAction implements Managed {
    @EqualsAndHashCode.Include
    private final String communicatorId;

    @EqualsAndHashCode.Include
    private final String actionId;

    @Getter
    private final ServiceMethodIdentifier targetServiceMethod;

    @Getter
    private final ValueFromModelMapper<?, ? extends Value> inputMapper;

    @Getter
    private final ValueToModelMapper<?, ? extends Value> outputMapper;

    @Getter
    private final MethodProcessingMode inputMode;

    @Getter
    private final MethodProcessingMode outputMode;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Object, Flux<Object>> adoptInput = adoptInput();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Object>, Object> adoptOutput = adoptOutput();

    private final ManagedValue<CommunicatorModuleConfiguration> moduleConfiguration = managed(this::moduleConfiguration);
    private final ManagedValue<Optional<CommunicatorProxyConfiguration>> communicatorConfiguration = managed(this::communicatorConfiguration);


    private final CommunicatorActionImplementation implementation;

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
        return cast(mapOutput(defer(() -> deferredCommunicate(input))
                .subscribeOn(communicatorConfiguration.get()
                        .map(CommunicatorProxyConfiguration::getScheduler)
                        .orElseGet(moduleConfiguration.get()::getScheduler))));
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
        Flux<Object> inputFlux = getAdoptInput().apply(input);
        for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
            inputFlux = inputFlux.transform(decorator);
        }
        return inputFlux.map(value -> inputMapper.map(cast(value)));
    }

    private Object mapOutput(Flux<Value> output) {
        Flux<Object> mappedOutput = output.map(value -> outputMapper.map(cast(value)));
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            mappedOutput = mappedOutput.transform(decorator);
        }
        return getAdoptOutput().apply(mappedOutput);
    }

    private Flux<Value> mapException(Throwable exception) {
        Flux<Object> errorOutput = error(exception);
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            errorOutput = errorOutput.transform(decorator);
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
                return Flux::next;
            case FLUX:
                return output -> output;
            default:
                throw new ImpossibleSituation();
        }
    }

    private Optional<CommunicatorProxyConfiguration> communicatorConfiguration() {
        return ofNullable(moduleConfiguration.get().getConfigurations().get(communicatorId));
    }

    private CommunicatorModuleConfiguration moduleConfiguration() {
        return communicatorModule().configuration();
    }
}
