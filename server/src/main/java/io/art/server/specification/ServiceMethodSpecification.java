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
import io.art.core.property.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.server.module.ServerModule.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;
import static reactor.core.publisher.Sinks.*;
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


    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter(lazy = true)
    private final ServerModuleConfiguration configuration = serverModule().configuration();

    private final LazyProperty<Function<Flux<Object>, Flux<Object>>> handler = lazy(this::selectHandler);

    public Flux<Object> serve(Flux<Object> input) {
        return decorateOutput(handler.get().apply(decorateInput(input)));
    }


    private Function<Flux<Object>, Flux<Object>> selectHandler() {
        if (isNull(inputType)) {
            return emptyInputHandler();
        }

        if (inputType.internalKind() == MONO) {
            return monoInputHandler();
        }

        if (inputType.internalKind() == FLUX) {
            return fluxInputHandler();
        }

        return blockingInputHandler();
    }


    private Function<Flux<Object>, Flux<Object>> emptyInputHandler() {
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return empty -> {
                method.invoke();
                return Flux.empty();
            };
        }

        if (outputType.internalKind() == MONO || outputType.internalKind() == FLUX) {
            return empty -> {
                Object output = method.invoke();
                if (isNull(output)) return Flux.empty();
                return from(asPublisher(output));
            };
        }

        return empty -> {
            Object output = method.invoke();
            if (isNull(output)) return Flux.empty();
            return just(output);
        };
    }

    private Function<Flux<Object>, Flux<Object>> monoInputHandler() {
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return input -> {
                subscribeMonoEmpty(input);
                return Flux.empty();
            };
        }

        if (outputType.internalKind() == MONO) {
            return input -> {
                Sinks.One<Object> result = one();
                subscribeMonoMono(input, result);
                return Flux.from(result.asMono());
            };
        }

        if (outputType.internalKind() == FLUX) {
            return input -> {
                Sinks.Many<Object> result = many().unicast().onBackpressureBuffer();
                subscribeMonoFlux(input, result);
                return result.asFlux();
            };
        }

        return input -> {
            Sinks.Many<Object> result = many().unicast().onBackpressureBuffer();
            subscribeMonoBlocking(input, result);
            return result.asFlux();
        };
    }

    private Function<Flux<Object>, Flux<Object>> fluxInputHandler() {
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return input -> {
                method.invoke(input);
                return Flux.empty();
            };
        }

        if (outputType.internalKind() == MONO) {
            return input -> {
                Object output = method.invoke(input);
                if (isNull(output)) return Flux.empty();
                return from(asMono(output));
            };
        }

        if (outputType.internalKind() == FLUX) {
            return input -> {
                Object output = method.invoke(input);
                if (isNull(output)) return Flux.empty();
                return asFlux(output);
            };
        }

        return input -> {
            Object output = method.invoke(input);
            if (isNull(output)) return Flux.empty();
            return just(output);
        };
    }

    private Function<Flux<Object>, Flux<Object>> blockingInputHandler() {
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return input -> {
                subscribeBlockingEmpty(input);
                return Flux.empty();
            };
        }

        if (outputType.internalKind() == MONO) {
            return input -> {
                Sinks.One<Object> result = one();
                subscribeBlockingMono(input, result);
                return from(result.asMono());
            };
        }

        if (outputType.internalKind() == FLUX) {
            return input -> {
                Sinks.Many<Object> result = many().unicast().onBackpressureBuffer();
                subscribeBlockingFlux(input, result);
                return result.asFlux();
            };
        }

        return input -> {
            Sinks.Many<Object> result = many().unicast().onBackpressureBuffer();
            subscribeBlockingBlocking(input, result);
            return result.asFlux();
        };
    }


    private void subscribeBlockingBlocking(Flux<Object> input, Many<Object> result) {
        input.subscribe(element -> emitBlockingOutput(result, element));
    }

    private void subscribeBlockingFlux(Flux<Object> input, Sinks.Many<Object> result) {
        input.subscribe(element -> emitFluxOutput(result, element));
    }

    private void subscribeBlockingMono(Flux<Object> input, Sinks.One<Object> result) {
        input.subscribe(element -> emitMonoOutput(result, element));
    }

    private void subscribeBlockingEmpty(Flux<Object> input) {
        input.subscribe(method::invoke);
    }


    private void subscribeMonoBlocking(Flux<Object> input, Sinks.Many<Object> result) {
        input.subscribe(element -> emitBlockingOutput(result, Mono.just(element)));
    }

    private void subscribeMonoFlux(Flux<Object> input, Sinks.Many<Object> result) {
        input.subscribe(element -> emitFluxOutput(result, Mono.just(element)));
    }

    private void subscribeMonoMono(Flux<Object> input, Sinks.One<Object> result) {
        input.subscribe(element -> emitMonoOutput(result, Mono.just(element)));
    }

    private void subscribeMonoEmpty(Flux<Object> input) {
        input.subscribe(element -> method.invoke(Mono.just(element)));
    }


    private void emitBlockingOutput(Sinks.Many<Object> result, Object element) {
        Object output = method.invoke(element);
        if (isNull(output)) return;
        result.emitNext(output, FAIL_FAST);
    }

    private void emitFluxOutput(Sinks.Many<Object> result, Object element) {
        Object output = method.invoke(element);
        if (isNull(output)) return;
        asFlux(output).subscribe(resultElement -> result.emitNext(resultElement, FAIL_FAST));
    }

    private void emitMonoOutput(Sinks.One<Object> result, Object element) {
        Object output = method.invoke(element);
        if (isNull(output)) return;
        asMono(output).subscribe(resultElement -> result.emitValue(resultElement, FAIL_FAST));
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
}
