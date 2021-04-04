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
import lombok.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.constants.MethodProcessingMode.*;
import static io.art.core.factory.ArrayFactory.*;
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

    @Getter
    private final ValueToModelMapper<?, ? extends Value> inputMapper;

    @Getter
    private final ValueFromModelMapper<?, ? extends Value> outputMapper;

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
    private final Function<Flux<Object>, Object> adoptInput = adoptInput();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Object, Flux<Object>> adoptOutput = adoptOutput();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Value>, Flux<Value>> adoptServe = adoptServe();

    @Getter(lazy = true, value = PRIVATE)
    private final ServerModuleConfiguration configuration = serverModule().configuration();

    @Getter(lazy = true, value = PRIVATE)
    private final Scheduler blockingScheduler = getConfiguration().getBlockingScheduler(serviceId, methodId);

    public Flux<Value> serve(Flux<Value> input) {
        return getAdoptServe().apply(input);
    }

    private Flux<Value> processServing(Flux<Value> input) {
        try {
            return transformOutput(implementation.serve(transformInput(input)));
        } catch (Throwable throwable) {
            return transformException(throwable);
        }
    }


    public Object transformInput(Flux<Value> input) {
        Flux<Object> inputFlux = input
                .map(value -> inputMapper.map(cast(value)))
                .transform(flux -> decorateInput(cast(flux)));
        return getAdoptInput().apply(inputFlux);
    }

    private Flux<Value> transformOutput(Object output) {
        return let(output, getAdoptOutput(), Flux.empty())
                .transform(this::decorateOutput)
                .map(value -> (Value) outputMapper.map(cast(value)))
                .onErrorResume(Throwable.class, this::transformException);
    }

    private Flux<Value> transformException(Throwable exception) {
        return Flux
                .error(exception)
                .transform(this::decorateOutput)
                .map(Caster::cast);
    }


    public Flux<Object> decorateInput(Flux<Object> input) {
        Flux<Object> result = input;
        for (UnaryOperator<Flux<Object>> decorator : beforeInputDecorators) {
            result = result.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
            result = result.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : afterInputDecorators) {
            result = result.transform(decorator);
        }
        return result;
    }

    public Flux<Object> decorateOutput(Flux<Object> output) {
        Flux<Object> result = output;
        for (UnaryOperator<Flux<Object>> decorator : beforeOutputDecorators) {
            result = result.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            result = result.transform(decorator);
        }
        for (UnaryOperator<Flux<Object>> decorator : afterOutputDecorators) {
            result = result.transform(decorator);
        }
        return result;
    }


    private Function<Flux<Object>, Object> adoptInput() {
        if (isNull(inputMode)) {
            throw new ImpossibleSituationException();
        }
        switch (inputMode) {
            case BLOCKING:
                return input -> input.next().block();
            case MONO:
                return Flux::next;
            case FLUX:
                return Caster::cast;
            default:
                throw new ImpossibleSituationException();
        }
    }

    private Function<Object, Flux<Object>> adoptOutput() {
        if (isNull(outputMode)) {
            throw new ImpossibleSituationException();
        }
        switch (outputMode) {
            case BLOCKING:
                return Flux::just;
            case MONO:
            case FLUX:
                return output -> Flux.from(cast(output));
            default:
                throw new ImpossibleSituationException();
        }
    }

    private Function<Flux<Value>, Flux<Value>> adoptServe() {
        if (inputMode == BLOCKING || outputMode == BLOCKING) {
            return input -> defer(() -> processServing(input)).subscribeOn(getBlockingScheduler());
        }
        return this::processServing;
    }
}
