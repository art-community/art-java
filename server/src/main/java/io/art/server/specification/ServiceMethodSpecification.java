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
import io.art.core.collection.*;
import io.art.core.constants.*;
import io.art.core.exception.*;
import io.art.server.configuration.*;
import io.art.server.decorator.*;
import io.art.server.implementation.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import io.art.value.mapping.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.server.module.ServerModule.*;
import static java.lang.Integer.MAX_VALUE;
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

    @Getter
    private final ValueToModelMapper<?, ? extends Value> inputMapper;

    @Getter
    private final ValueFromModelMapper<?, ? extends Value> outputMapper;

    @Builder.Default
    private final ValueFromModelMapper<Throwable, ? extends Value> exceptionMapper = ThrowableMapping::fromThrowableNested;

    private final ServiceMethodImplementation implementation;

    private final ImmutableArray<UnaryOperator<Flux<Object>>> beforeInputDecorators = immutableArrayOf(
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> afterInputDecorators = immutableArrayOf(
            new ServiceLoggingDecorator(this, INPUT),
            new ServiceDeactivationDecorator(this),
            new ServiceStateDecorator(this)
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> beforeOutputDecorators = immutableArrayOf(
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> afterOutputDecorators = immutableArrayOf(
            new ServiceLoggingDecorator(this, OUTPUT),
            new ServiceDeactivationDecorator(this)
    );

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Value>, Flux<Value>> execution = execution();

    @Getter(lazy = true, value = PRIVATE)
    private final ServerModuleConfiguration configuration = serverModule().configuration();

    public Flux<Value> serve(Flux<Value> input) {
        return execution().apply(input);
    }

    private Function<Flux<Value>, Flux<Value>> execution() {
        if (isNull(inputMode)) throw new ImpossibleSituationException();
        switch (inputMode) {
            case BLOCKING:
                return input -> orElse(input, Flux.<Value>empty())
                        .transform(this::mapInput)
                        .flatMap(payload -> mapOutput(implementation.execute(payload)))
                        .switchIfEmpty(defer(() -> mapOutput(implementation.execute(null))))
                        .onErrorResume(Throwable.class, this::mapException);
            case MONO:
                return input -> orElse(input, Flux.<Value>empty())
                        .transformDeferred(this::mapInput)
                        .next()
                        .transformDeferred(mono -> mapOutput(implementation.execute(mono)))
                        .flux()
                        .onErrorResume(Throwable.class, this::mapException);
            case FLUX:
                return input -> orElse(input, Flux.<Value>empty())
                        .transformDeferred(this::mapInput)
                        .transformDeferred(mono -> mapOutput(implementation.execute(mono)))
                        .onErrorResume(Throwable.class, this::mapException);
        }
        throw new ImpossibleSituationException();
    }

    private Flux<Object> mapInput(Flux<Value> input) {
        Flux<Object> inputFlux = input.map(value -> inputMapper.map(cast(value)));
        for (UnaryOperator<Flux<Object>> decorator : beforeInputDecorators) {
            inputFlux = inputFlux.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
            inputFlux = inputFlux.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : afterInputDecorators) {
            inputFlux = inputFlux.transform(decorator);
        }
        return inputFlux;
    }

    private Flux<Value> mapOutput(Object output) {
        Flux<Object> outputFlux = adoptFlux(outputMode, output);
        for (UnaryOperator<Flux<Object>> decorator : beforeOutputDecorators) {
            outputFlux = outputFlux.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            outputFlux = outputFlux.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : afterOutputDecorators) {
            outputFlux = outputFlux.transform(decorator);
        }
        return outputFlux.map(value -> outputMapper.map(cast(value)));
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
        return errorOutput
                .onErrorResume(Throwable.class, throwable -> just(exceptionMapper.map(throwable)))
                .map(Caster::cast);

    }
}
