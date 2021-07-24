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
import io.art.core.exception.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.server.decorator.*;
import lombok.*;
import org.reactivestreams.*;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.core.publisher.Sinks.*;
import reactor.core.scheduler.*;
import reactor.util.context.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.constants.MethodProcessingMode.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.FLUX;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.MONO;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.server.constants.ServerModuleConstants.StateKeys.*;
import static io.art.server.module.ServerModule.*;
import static io.art.server.state.ServerModuleState.ServerThreadLocalState.*;
import static java.util.Objects.*;
import static java.util.function.UnaryOperator.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.empty;
import static reactor.core.publisher.Flux.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;
import java.util.*;
import java.util.function.*;

@Builder(toBuilder = true)
@ForGenerator
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceMethodSpecification {
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

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Object>, Flux<Object>> adoptInput = adoptInput();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Object>, Flux<Object>> adoptOutput = adoptOutput();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Object>, Flux<Object>> adoptServe = adoptServe();

    @Getter(lazy = true, value = PRIVATE)
    private final Scheduler blockingScheduler = getConfiguration().getBlockingScheduler(serviceMethod(serviceId, methodId));


    public Flux<Object> serve(Flux<Object> input) {
        if (isNull(inputType)) {
            return adoptOutput(method.invoke());
        }

        if (inputType.internalKind() == MONO) {
            if (isNull(outputType)) {
                input.doOnNext(method::invoke).subscribe();
                return Flux.empty();
            }

            if (outputType.internalKind() == MONO) {
                One<Object> one = Sinks.one();
                CoreSubscriber<Mono<Object>> subscriber = new CoreSubscriber<>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Mono<Object> o) {
                        Mono<Object> result = (Mono<Object>) method.invoke(o);
                        result.doOnNext(element -> one.emitValue(element, FAIL_FAST)).subscribe();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
                input.map(Mono::just).subscribe(subscriber);
                return one.asMono().flux();
            }

            if (outputType.internalKind() == FLUX) {
                Many<Object> one = Sinks.many().unicast().onBackpressureBuffer();
                CoreSubscriber<Mono<Object>> subscriber = new CoreSubscriber<>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Mono<Object> o) {
                        Flux<Object> result = (Flux<Object>) method.invoke(o);
                        result.doOnNext(element -> one.emitNext(element, FAIL_FAST)).subscribe();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
                input.map(Mono::just).subscribe(subscriber);
                return one.asFlux();
            }

        }

        if (inputType.internalKind() == FLUX) {
            if (isNull(outputType) || outputType.internalKind() == VOID) {
                method.invoke(input);
                return Flux.empty();
            }

            if (outputType.internalKind() == MONO) {
                return from(cast(method.invoke(input)));
            }

            if (outputType.internalKind() == FLUX) {
                return cast(method.invoke(input));
            }
        }

        return never();
    }

    private Flux<Object> adoptOutput(Object output) {
        if (isNull(outputType) || isNull(output)) {
            return empty();
        }
        if (outputType.internalKind() == MONO || outputType.internalKind() == FLUX) {
            return from((Publisher<?>) output);
        }
        return Flux.just(output);
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

    private Function<Flux<Object>, Flux<Object>> adoptInput() {
        if (isNull(inputMode)) {
            throw new ImpossibleSituationException();
        }
        switch (inputMode) {
            case BLOCKING:
            case EMPTY:
                return identity();
            case MONO:
                return input -> input.map(Mono::just);
            case FLUX:
                return Flux::just;
            default:
                throw new ImpossibleSituationException();
        }
    }

    private Function<Flux<Object>, Flux<Object>> adoptOutput() {
        if (isNull(outputMode)) {
            throw new ImpossibleSituationException();
        }
        switch (outputMode) {
            case BLOCKING:
                return identity();
            case MONO:
            case FLUX:
                return input -> {
                    Flux<Publisher<Object>> casted = cast(input);
                    return Flux.concat(casted);
                };
            default:
                throw new ImpossibleSituationException();
        }
    }

    private Function<Flux<Object>, Flux<Object>> adoptServe() {
        if (inputMode == EMPTY) {
            return input -> input
                    .publishOn(getBlockingScheduler())
                    .transformDeferredContextual((flux, ctx) -> flux
                            .mapNotNull(entry -> processServing(entry, ctx))
                            .defaultIfEmpty(processServing(null, ctx))
                    );
        }

        if (inputMode == BLOCKING || outputMode == BLOCKING) {
            return input -> input
                    .publishOn(getBlockingScheduler())
                    .transformDeferredContextual((flux, ctx) -> flux.mapNotNull(entry -> processServing(entry, ctx)));
        }

        return input -> input
                .transformDeferredContextual((flux, ctx) -> flux.mapNotNull(entry -> processServing(entry, ctx)));
    }

    private Object processServing(Object input, ContextView context) {
        if (context.hasKey(SPECIFICATION_KEY)) serverModule().state().localState(fromContext(context));
        return method.serve(input);
    }
}
