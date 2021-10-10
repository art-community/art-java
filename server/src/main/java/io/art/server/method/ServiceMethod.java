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

package io.art.server.method;

import io.art.core.model.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import static reactor.core.publisher.Sinks.*;
import java.util.*;
import java.util.function.*;

@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceMethod {
    @Getter
    @EqualsAndHashCode.Include
    private final ServiceMethodIdentifier id;

    @Getter
    private final MetaMethodInvoker invoker;

    @Getter
    private final MetaType<?> inputType;

    @Getter
    private final MetaType<?> outputType;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Object>, Flux<Object>> handler = selectHandler();

    public Flux<Object> serve(Flux<Object> input) {
        return decorateOutput(getHandler().apply(decorateInput(input)));
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
            return input -> {
                Sinks.One<Object> sink = one();
                subscribeEmptyEmpty(input, sink);
                return Flux.from(sink.asMono());
            };
        }

        if (outputType.internalKind() == MONO) {
            return input -> {
                Sinks.One<Object> result = one();
                subscribeEmptyMono(input, result);
                return from(result.asMono());
            };
        }

        if (outputType.internalKind() == FLUX) {
            return input -> {
                Sinks.Many<Object> sink = many().unicast().onBackpressureBuffer();
                subscribeEmptyFlux(input, sink);
                return sink.asFlux();
            };
        }

        return input -> {
            Sinks.Many<Object> sink = many().unicast().onBackpressureBuffer();
            subscribeEmptyBlocking(input, sink);
            return sink.asFlux();
        };
    }

    private Function<Flux<Object>, Flux<Object>> monoInputHandler() {
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return input -> {
                Sinks.One<Object> sink = one();
                subscribeMonoEmpty(input, sink);
                return Flux.from(sink.asMono());
            };
        }

        if (outputType.internalKind() == MONO) {
            return input -> {
                Sinks.One<Object> sink = one();
                subscribeMonoMono(input, sink);
                return Flux.from(sink.asMono());
            };
        }

        if (outputType.internalKind() == FLUX) {
            return input -> {
                Sinks.Many<Object> sink = many().unicast().onBackpressureBuffer();
                subscribeMonoFlux(input, sink);
                return sink.asFlux();
            };
        }

        return input -> {
            Sinks.Many<Object> sink = many().unicast().onBackpressureBuffer();
            subscribeMonoBlocking(input, sink);
            return sink.asFlux();
        };
    }

    private Function<Flux<Object>, Flux<Object>> fluxInputHandler() {
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return input -> {
                try {
                    invoker.invoke(input);
                    return Flux.empty();
                } catch (Throwable throwable) {
                    return Flux.error(throwable);
                }
            };
        }

        if (outputType.internalKind() == MONO) {
            return input -> {
                try {
                    Object output = invoker.invoke(input);
                    if (isNull(output)) return Flux.empty();
                    return from(asMono(output));
                } catch (Throwable throwable) {
                    return Flux.error(throwable);
                }
            };
        }

        if (outputType.internalKind() == FLUX) {
            return input -> {
                try {
                    Object output = invoker.invoke(input);
                    if (isNull(output)) return Flux.empty();
                    return asFlux(output);
                } catch (Throwable throwable) {
                    return Flux.error(throwable);
                }
            };
        }

        return input -> {
            try {
                Object output = invoker.invoke(input);
                if (isNull(output)) return Flux.empty();
                return just(output);
            } catch (Throwable throwable) {
                return Flux.error(throwable);
            }
        };
    }

    private Function<Flux<Object>, Flux<Object>> blockingInputHandler() {
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return input -> {
                Sinks.One<Object> sink = one();
                subscribeBlockingEmpty(input, sink);
                return Flux.from(sink.asMono());
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
                Sinks.Many<Object> sink = many().unicast().onBackpressureBuffer();
                subscribeBlockingFlux(input, sink);
                return sink.asFlux();
            };
        }

        return input -> {
            Sinks.Many<Object> sink = many().unicast().onBackpressureBuffer();
            subscribeBlockingBlocking(input, sink);
            return sink.asFlux();
        };
    }


    private void subscribeEmptyBlocking(Flux<Object> input, Sinks.Many<Object> sink) {
        input.subscribe(emptyConsumer(), sink::tryEmitError, () -> callBlockingOutput(sink));
    }

    private void subscribeEmptyFlux(Flux<Object> input, Sinks.Many<Object> sink) {
        input.subscribe(emptyConsumer(), sink::tryEmitError, () -> callFluxOutput(sink));
    }

    private void subscribeEmptyMono(Flux<Object> input, Sinks.One<Object> sink) {
        input.subscribe(emptyConsumer(), sink::tryEmitError, () -> callMonoOutput(sink));
    }

    private void subscribeEmptyEmpty(Flux<Object> input, Sinks.One<Object> sink) {
        input.subscribe(emptyConsumer(), sink::tryEmitError, () -> callEmptyOutput(sink));
    }


    private void subscribeBlockingBlocking(Flux<Object> input, Sinks.Many<Object> sink) {
        input.subscribe(element -> emitBlockingOutput(element, sink), sink::tryEmitError, sink::tryEmitComplete);
    }

    private void subscribeBlockingFlux(Flux<Object> input, Sinks.Many<Object> sink) {
        input.subscribe(element -> emitFluxOutput(element, sink), sink::tryEmitError, sink::tryEmitComplete);
        ;
    }

    private void subscribeBlockingMono(Flux<Object> input, Sinks.One<Object> sink) {
        input.subscribe(element -> emitMonoOutput(element, sink), sink::tryEmitError, sink::tryEmitEmpty);
    }

    private void subscribeBlockingEmpty(Flux<Object> input, Sinks.One<Object> sink) {
        input.subscribe(element -> emitEmptyOutput(element, sink), sink::tryEmitError, sink::tryEmitEmpty);
    }


    private void subscribeMonoBlocking(Flux<Object> input, Sinks.Many<Object> sink) {
        input.subscribe(element -> emitBlockingOutput(Mono.just(element), sink), sink::tryEmitError, sink::tryEmitComplete);
    }

    private void subscribeMonoFlux(Flux<Object> input, Sinks.Many<Object> sink) {
        input.subscribe(element -> emitFluxOutput(Mono.just(element), sink), sink::tryEmitError, sink::tryEmitComplete);
    }

    private void subscribeMonoMono(Flux<Object> input, Sinks.One<Object> sink) {
        input.subscribe(element -> emitMonoOutput(Mono.just(element), sink), sink::tryEmitError, sink::tryEmitEmpty);
    }

    private void subscribeMonoEmpty(Flux<Object> input, Sinks.One<Object> sink) {
        input.subscribe(element -> emitEmptyOutput(Mono.just(element), sink), sink::tryEmitError, sink::tryEmitEmpty);
    }


    private void emitEmptyOutput(Object element, Sinks.One<Object> sink) {
        try {
            invoker.invoke(element);
            sink.tryEmitEmpty();
        } catch (Throwable throwable) {
            sink.tryEmitError(throwable);
        }
    }

    private void emitBlockingOutput(Object element, Sinks.Many<Object> sink) {
        try {
            Object output = invoker.invoke(element);
            if (isNull(output)) {
                sink.tryEmitComplete();
                return;
            }
            sink.tryEmitNext(output);
            sink.tryEmitComplete();
        } catch (Throwable throwable) {
            sink.tryEmitError(throwable);
            sink.tryEmitComplete();
        }
    }

    private void emitFluxOutput(Object element, Sinks.Many<Object> sink) {
        try {
            Object output = invoker.invoke(element);
            if (isNull(output)) {
                sink.tryEmitComplete();
                return;
            }
            asFlux(output).subscribe(sink::tryEmitNext, sink::tryEmitError, sink::tryEmitComplete);
        } catch (Throwable throwable) {
            sink.tryEmitError(throwable);
            sink.tryEmitComplete();
        }
    }

    private void emitMonoOutput(Object element, Sinks.One<Object> sink) {
        try {
            Object output = invoker.invoke(element);
            if (isNull(output)) {
                sink.tryEmitEmpty();
                return;
            }
            asMono(output).subscribe(sink::tryEmitValue, sink::tryEmitError);
        } catch (Throwable throwable) {
            sink.tryEmitError(throwable);
        }
    }


    private void callEmptyOutput(Sinks.One<Object> sink) {
        try {
            invoker.invoke();
            sink.tryEmitEmpty();
        } catch (Throwable throwable) {
            sink.tryEmitError(throwable);
        }
    }

    private void callBlockingOutput(Sinks.Many<Object> sink) {
        try {
            Object output = invoker.invoke();
            if (isNull(output)) {
                sink.tryEmitComplete();
                return;
            }
            sink.tryEmitNext(output);
            sink.tryEmitComplete();
        } catch (Throwable throwable) {
            sink.tryEmitError(throwable);
            sink.tryEmitComplete();
        }
    }

    private void callFluxOutput(Sinks.Many<Object> sink) {
        try {
            Object output = invoker.invoke();
            if (isNull(output)) {
                sink.tryEmitComplete();
                return;
            }
            asFlux(output).subscribe(sink::tryEmitNext, sink::tryEmitError, sink::tryEmitComplete);
        } catch (Throwable throwable) {
            sink.tryEmitError(throwable);
            sink.tryEmitComplete();
        }
    }

    private void callMonoOutput(Sinks.One<Object> sink) {
        try {
            Object output = invoker.invoke();
            if (isNull(output)) {
                sink.tryEmitEmpty();
                return;
            }
            asMono(output)
                    .doOnSuccess(sink::tryEmitValue)
                    .doOnError(sink::tryEmitError)
                    .subscribe();
        } catch (Throwable throwable) {
            sink.tryEmitError(throwable);
        }
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
