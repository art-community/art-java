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

package io.art.server.specification;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.decorator.*;
import lombok.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.server.module.ServerModule.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;
import java.util.*;
import java.util.function.*;

@Builder(toBuilder = true)
@ForGenerator
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceMethodSpecification {
    @Getter
    private final String serviceId;

    @Getter
    private final String methodId;

    @Getter
    private final MetaType<?> inputType;

    @Getter
    private final MetaType<?> outputType;

    private final MetaMethodInvoker method;

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

    @Getter(lazy = true)
    private final ServerModuleConfiguration configuration = serverModule().configuration();

    public Flux<Object> serve(Flux<Object> input) {
        return decorateOutput(process(decorateInput(input)));
    }

    private Flux<Object> process(Flux<Object> input) {
        if (isNull(inputType)) {
            Object output = method.invoke();

            if (isNull(outputType) || isNull(output)) {
                return empty();
            }

            if (outputType.internalKind() == MONO || outputType.internalKind() == FLUX) {
                return from((Publisher<?>) output);
            }

            return Flux.just(output);
        }

        if (inputType.internalKind() == MONO) {
            if (isNull(outputType)) {
                input.subscribe(element -> method.invoke(Mono.just(element)));
                return Flux.empty();
            }

            if (outputType.internalKind() == MONO) {
                Sinks.One<Object> result = Sinks.one();
                input.subscribe(element -> asMono(method.invoke(Mono.just(element))).subscribe(resultElement -> result.emitValue(resultElement, FAIL_FAST)));
                return result.asMono().flux();
            }

            if (outputType.internalKind() == FLUX) {
                Sinks.Many<Object> result = Sinks.many().unicast().onBackpressureBuffer();
                input.subscribe(element -> asFlux(method.invoke(Mono.just(element))).subscribe(resultElement -> result.emitNext(resultElement, FAIL_FAST)));
                return result.asFlux();
            }

            Sinks.Many<Object> result = Sinks.many().unicast().onBackpressureBuffer();
            input.subscribe(element -> result.emitNext(method.invoke(Mono.just(element)), FAIL_FAST));
            return result.asFlux();
        }

        if (inputType.internalKind() == FLUX) {
            if (isNull(outputType)) {
                method.invoke(input);
                return Flux.empty();
            }

            if (outputType.internalKind() == MONO) {
                return asMono(method.invoke(input)).flux();
            }

            if (outputType.internalKind() == FLUX) {
                return asFlux(method.invoke(input));
            }

            return Flux.just(method.invoke(input));
        }

        return never();
    }

    private Flux<Object> decorateInput(Flux<Object> input) {
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

    private Flux<Object> decorateOutput(Flux<Object> output) {
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
}
