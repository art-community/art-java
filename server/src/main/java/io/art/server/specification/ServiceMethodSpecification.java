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
import lombok.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
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
    private final MetaMethodInvoker method;

    @Getter
    private final MetaType<?> inputType;

    @Getter
    private final MetaType<?> outputType;

    public String getServiceId() {
        return method.getOwner().definition().type().getName();
    }

    public String getMethodId() {
        return method.getDelegate().name();
    }


    private final ImmutableArray<UnaryOperator<Flux<Object>>> beforeInputDecorators = immutableArrayOf(
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> afterInputDecorators = immutableArrayOf(
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> beforeOutputDecorators = immutableArrayOf(
    );

    private final ImmutableArray<UnaryOperator<Flux<Object>>> afterOutputDecorators = immutableArrayOf(
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
                input.subscribe(element -> processMono(result, element));
                return result.asMono().flux();
            }

            if (outputType.internalKind() == FLUX) {
                Sinks.Many<Object> result = Sinks.many().unicast().onBackpressureBuffer();
                input.subscribe(element -> processFlux(result, element));
                return result.asFlux();
            }

            Sinks.Many<Object> result = Sinks.many().unicast().onBackpressureBuffer();
            input.subscribe(element -> processBlocking(result, element));
            return result.asFlux();
        }

        if (inputType.internalKind() == FLUX) {
            Object output = method.invoke(input);

            if (isNull(outputType) || isNull(output)) {
                return Flux.empty();
            }

            if (outputType.internalKind() == MONO) {
                return asMono(output).flux();
            }

            if (outputType.internalKind() == FLUX) {
                return asFlux(output);
            }

            return Flux.just(output);
        }

        return never();
    }

    private void processBlocking(Sinks.Many<Object> result, Object element) {
        Object output = method.invoke(Mono.just(element));
        if (isNull(output)) return;
        result.emitNext(output, FAIL_FAST);
    }

    private void processFlux(Sinks.Many<Object> result, Object element) {
        Object output = method.invoke(Mono.just(element));
        if (isNull(output)) return;
        asFlux(output).subscribe(resultElement -> processBlocking(result, resultElement));
    }

    private void processMono(Sinks.One<Object> result, Object element) {
        Object output = method.invoke(Mono.just(element));
        if (isNull(output)) return;
        asMono(output).subscribe(resultElement -> result.emitValue(resultElement, FAIL_FAST));
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
