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
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;

@Builder
@UsedByGenerator
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunicatorSpecification {
    @EqualsAndHashCode.Include
    private final String communicatorId;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Singular("exceptionDecorator")
    private final List<UnaryOperator<Flux<Object>>> exceptionDecorators;

    private final ValueFromModelMapper<?, ? extends Value> inputMapper;
    private final ValueToModelMapper<?, ? extends Value> outputMapper;
    private final CommunicatorImplementation implementation;
    private final MethodProcessingMode inputMode;
    private final MethodProcessingMode outputMode;

    @Getter(lazy = true)
    private final CommunicatorModuleConfiguration moduleConfiguration = communicatorModule().configuration();

    @Getter(lazy = true)
    private final CommunicatorConfiguration communicatorConfiguration = getModuleConfiguration().getConfigurations().get(communicatorId);

    @Getter(lazy = true)
    private final Function<Object, Flux<Object>> mapInput = selectMapInput();

    @Getter(lazy = true)
    private final Function<Flux<Object>, Object> mapOutput = selectMapOutput();

    public <T> T communicate() {
        return communicate(null);
    }

    public <T> T communicate(Object input) {
        Scheduler scheduler = let(getCommunicatorConfiguration(), CommunicatorConfiguration::getScheduler, getModuleConfiguration().getScheduler());
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
        Flux<Object> inputFlux = getMapInput().apply(input).filter(Objects::nonNull);
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
        return getMapOutput().apply(mappedOutput);
    }

    private Flux<Value> mapException(Throwable exception) {
        Flux<Object> errorOutput = error(exception);
        for (UnaryOperator<Flux<Object>> decorator : exceptionDecorators) {
            errorOutput = errorOutput.transformDeferred(decorator);
        }
        return cast(errorOutput);
    }

    private Function<Object, Flux<Object>> selectMapInput() {
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

    private Function<Flux<Object>, Object> selectMapOutput() {
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
}
