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
import io.art.communicator.exception.*;
import io.art.communicator.implementation.*;
import io.art.core.annotation.*;
import io.art.core.constants.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.ExceptionMessages.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static java.text.MessageFormat.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;

@Getter
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

    public <T> T communicate() {
        Scheduler scheduler = let(getCommunicatorConfiguration(), CommunicatorConfiguration::getScheduler, getModuleConfiguration().getScheduler());
        return cast(mapOutput(defer(() -> deferredCommunicate(null)).subscribeOn(scheduler)));
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
        Flux<Object> inputFlux;
        switch (inputMode) {
            case BLOCKING:
                inputFlux = just(input);
                break;
            case MONO:
            case FLUX:
                inputFlux = from(cast(input));
                break;
            default:
                throw new CommunicatorException(format(UNKNOWN_INPUT_MODE, inputMode), getCommunicatorId());
        }
        inputFlux = inputFlux.filter(Objects::nonNull);
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
        switch (outputMode) {
            case BLOCKING:
                return mappedOutput.blockFirst();
            case MONO:
                return mappedOutput.last();
            case FLUX:
                return mappedOutput;
            default:
                throw new CommunicatorException(format(UNKNOWN_OUTPUT_MODE, outputMode), getCommunicatorId());
        }
    }

    private Flux<Value> mapException(Throwable exception) {
        Flux<Object> errorOutput = error(exception);
        for (UnaryOperator<Flux<Object>> decorator : exceptionDecorators) {
            errorOutput = errorOutput.transformDeferred(decorator);
        }
        return cast(errorOutput);
    }
}
