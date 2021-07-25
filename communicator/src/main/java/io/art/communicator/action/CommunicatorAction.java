/*
 * ART
 *
 * Copyright 2019-2021 ART
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
import io.art.communicator.exception.*;
import io.art.communicator.implementation.*;
import io.art.core.managed.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.property.Property.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunicatorAction implements Managed {
    @Getter
    @EqualsAndHashCode.Include
    private final CommunicatorActionIdentifier id;

    @Getter
    private final MetaType<?> inputType;

    @Getter
    private final MetaType<?> outputType;

    @Getter
    private final MetaType<?> exceptionType;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter(lazy = true, value = PRIVATE)
    private final CommunicatorModuleConfiguration configuration = communicatorModule().configuration();

    private final Property<Optional<CommunicatorProxyConfiguration>> communicatorConfiguration = property(this::communicatorConfiguration);

    @Getter
    private final CommunicatorActionImplementation implementation;

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
        Flux<Object> output = implementation.communicate(Flux.empty());
        if (isNull(outputType) || outputType.internalKind() == VOID || isNull(output)) {
            return null;
        }

        if (outputType.internalKind() == MONO) {
            return cast(output.last());
        }

        if (outputType.internalKind() == FLUX) {
            return cast(output);
        }

        return cast(blockFirst(output));
    }

    public <T> T communicate(Object input) {
        if (isNull(inputType)) {
            return communicate();
        }

        if (inputType.internalKind() == MONO) {
            Flux<Object> output = implementation.communicate(asMono(input).flux());
            if (isNull(outputType) || outputType.internalKind() == VOID) {
                if (isNull(output)) {
                    return null;
                }
            }

            if (outputType.internalKind() == MONO) {
                return cast(output.last());
            }

            if (outputType.internalKind() == FLUX) {
                return cast(output);
            }

            return cast(blockFirst(output));
        }

        if (inputType.internalKind() == FLUX) {
            Flux<Object> output = implementation.communicate(asFlux(input));
            if (isNull(outputType) || outputType.internalKind() == VOID) {
                if (isNull(output)) {
                    return null;
                }
            }

            if (outputType.internalKind() == MONO) {
                return cast(output.last());
            }

            if (outputType.internalKind() == FLUX) {
                return cast(output);
            }

            return cast(blockFirst(output));
        }

        Flux<Object> output = implementation.communicate(Flux.just(input));
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            if (isNull(output)) {
                return null;
            }
        }

        if (outputType.internalKind() == MONO) {
            return cast(output.last());
        }

        if (outputType.internalKind() == FLUX) {
            return cast(output);
        }

        return cast(blockFirst(output));
    }

    private Flux<Object> decorateInput(Flux<Object> input) {
        Flux<Object> result = input;
        if (isNotEmpty(inputDecorators)) {
            for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
                result = decorator.apply(result);
            }
        }
        return result;
    }

    private Flux<Object> decorateOutput(Flux<Object> output) {
        Flux<Object> result = output;
        if (isNotEmpty(outputDecorators)) {
            for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
                result = decorator.apply(result);
            }
        }
        return result;
    }


    private Flux<Object> mapException(Throwable throwable) {
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        throw new CommunicationException(throwable);
    }

    private Optional<CommunicatorProxyConfiguration> communicatorConfiguration() {
        return ofNullable(getConfiguration().getConfigurations().get(id));
    }

}
