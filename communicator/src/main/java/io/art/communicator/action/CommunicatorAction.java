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
import io.art.communicator.decorator.*;
import io.art.communicator.exception.*;
import io.art.communicator.implementation.*;
import io.art.communicator.mapper.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.constants.*;
import io.art.core.exception.*;
import io.art.core.managed.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;
import static io.art.communicator.mapper.CommunicatorExceptionMapper.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.property.Property.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.empty;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;

@UsedByGenerator
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunicatorAction implements Managed {
    @Getter
    @EqualsAndHashCode.Include
    private final String communicatorId;

    @Getter
    @EqualsAndHashCode.Include
    private final String actionId;

    @Getter
    private final ServiceMethodIdentifier targetServiceMethod;

    @Getter
    private final ValueFromModelMapper<?, ? extends Value> inputMapper;

    @Getter
    private final ValueToModelMapper<?, ? extends Value> outputMapper;

    @Getter
    @Builder.Default
    private final CommunicatorExceptionMapper exceptionMapper = communicatorThrowableExceptionMapper();

    @Getter
    private final MethodProcessingMode inputMode;

    @Getter
    private final MethodProcessingMode outputMode;

    private final ImmutableArray<UnaryOperator<Flux<Object>>> beforeInputDecorators = immutableArrayOf(
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> afterInputDecorators = immutableArrayOf(
            new CommunicatorLoggingDecorator(this, INPUT),
            new CommunicatorResilienceDecorator(this),
            new CommunicatorDeactivationDecorator(this)
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> beforeOutputDecorators = immutableArrayOf(
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> afterOutputDecorators = immutableArrayOf(
            new CommunicatorLoggingDecorator(this, OUTPUT),
            new CommunicatorResilienceDecorator(this),
            new CommunicatorDeactivationDecorator(this)
    );

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Object, Flux<Object>> adoptInput = adoptInput();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Object>, Object> adoptOutput = adoptOutput();

    @Getter(lazy = true, value = PRIVATE)
    private final CommunicatorModuleConfiguration configuration = communicatorModule().configuration();

    private final Property<Optional<CommunicatorProxyConfiguration>> communicatorConfiguration = property(this::communicatorConfiguration);

    @Getter
    private final CommunicatorActionImplementation implementation;

    @Getter(lazy = true, value = PRIVATE)
    private final Scheduler scheduler = getConfiguration().getScheduler(communicatorId, actionId);

    @Override
    public void initialize() {
        communicatorConfiguration.initialize();
        implementation.initialize();
    }

    @Override
    public void dispose() {
        implementation.dispose();
        communicatorConfiguration.dispose();
    }

    public <T> T communicate() {
        return communicate(null);
    }

    public <T> T communicate(Object input) {
        return cast(mapOutput(defer(() -> deferredCommunicate(input)).subscribeOn(getScheduler())));
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
        for (UnaryOperator<Flux<Object>> decorator : beforeInputDecorators) {
            inputFlux = inputFlux.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
            inputFlux = inputFlux.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : afterInputDecorators) {
            inputFlux = inputFlux.transform(decorator);
        }
        return inputFlux.map(value -> inputMapper.map(cast(value)));
    }

    private Object mapOutput(Flux<Value> output) {
        Flux<Object> outputFlux = output.map(this::mapOutput);
        for (UnaryOperator<Flux<Object>> decorator : beforeOutputDecorators) {
            outputFlux = outputFlux.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            outputFlux = outputFlux.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : afterOutputDecorators) {
            outputFlux = outputFlux.transform(decorator);
        }
        return getAdoptOutput().apply(outputFlux);
    }

    private Object mapOutput(Value value) {
        if (exceptionMapper.getFilter().apply(value)) {
            Throwable throwable = exceptionMapper.getMapper().map(cast(value));
            if (throwable instanceof Error) {
                throw (Error) throwable;
            }
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new CommunicationException(throwable);
        }
        return outputMapper.map(cast(value));
    }

    private Flux<Value> mapException(Throwable exception) {
        Flux<Object> errorOutput = error(exception);
        for (UnaryOperator<Flux<Object>> decorator : beforeOutputDecorators) {
            errorOutput = errorOutput.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            errorOutput = errorOutput.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : afterOutputDecorators) {
            errorOutput = errorOutput.transform(decorator);
        }
        return cast(errorOutput);
    }

    private Function<Object, Flux<Object>> adoptInput() {
        if (isNull(inputMode)) throw new ImpossibleSituationException();
        switch (inputMode) {
            case BLOCKING:
                return Flux::just;
            case MONO:
            case FLUX:
                return input -> from(cast(input));
            default:
                throw new ImpossibleSituationException();
        }
    }

    private Function<Flux<Object>, Object> adoptOutput() {
        if (isNull(outputMode)) throw new ImpossibleSituationException();
        switch (outputMode) {
            case BLOCKING:
                return output -> output.next().block();
            case MONO:
                return Flux::next;
            case FLUX:
                return output -> output;
            default:
                throw new ImpossibleSituationException();
        }
    }

    private Optional<CommunicatorProxyConfiguration> communicatorConfiguration() {
        return ofNullable(getConfiguration().getConfigurations().get(communicatorId));
    }
}
