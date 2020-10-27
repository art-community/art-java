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
import io.art.core.constants.*;
import io.art.entity.immutable.Value;
import io.art.entity.mapper.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.caster.Caster.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunicatorSpecification {
    @EqualsAndHashCode.Include
    private final String communicatorId;
    @EqualsAndHashCode.Include
    private final String methodId;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    private final ValueFromModelMapper<Object, Value> inputMapper;
    private final ValueToModelMapper<Object, Value> outputMapper;
    private final ValueFromModelMapper<Throwable, Value> exceptionMapper;
    private final CommunicatorImplementation implementation;
    private final MethodProcessingMode inputMode;
    private final MethodProcessingMode outputMode;
    private final CommunicatorModuleConfiguration moduleConfiguration = communicatorModule().configuration();

    @Getter(lazy = true)
    private final CommunicatorConfiguration communicatorConfiguration = moduleConfiguration.getCommunicators().get(communicatorId);

    public Object communicate(Object input) {
        return mapOutput(Flux.defer(() -> deferredCommunicate(input)).subscribeOn(getCommunicatorConfiguration().getScheduler()));
    }

    private Flux<Value> deferredCommunicate(Object input) {
        try {
            Flux<Value> output = implementation.communicate(mapInput(input));
            if (isNull(output)) {
                return Flux.empty();
            }
            return output;
        } catch (Throwable throwable) {
            return mapException(throwable);
        }
    }

    private Flux<Value> mapInput(Object input) {
        Flux<Object> inputFlux = Flux.empty();
        switch (inputMode) {
            case BLOCKING:
                inputFlux = Flux.just(input);
                break;
            case MONO:
            case FLUX:
                inputFlux = Flux.from(cast(input));
        }
        inputFlux = inputFlux.filter(Objects::nonNull);
        for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
            inputFlux = inputFlux.transformDeferred(decorator);
        }
        return inputFlux
                .map(inputMapper::map)
                .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
    }

    private Object mapOutput(Flux<Value> output) {
        Flux<Object> mappedOutput = output.filter(Objects::nonNull).map(outputMapper::map);
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            mappedOutput = mappedOutput.transformDeferred(decorator);
        }
        mappedOutput = mappedOutput.onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)));
        switch (outputMode) {
            case BLOCKING:
                return mappedOutput.blockFirst();
            case MONO:
                return mappedOutput.last();
            default:
                return mappedOutput;
        }
    }

    private Flux<Value> mapException(Throwable exception) {
        Flux<Object> errorOutput = Flux.error(exception);
        for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
            errorOutput = errorOutput.transformDeferred(decorator);
        }
        return errorOutput
                .onErrorResume(Throwable.class, throwable -> Flux.just(exceptionMapper.map(throwable)))
                .cast(Value.class);
    }
}
