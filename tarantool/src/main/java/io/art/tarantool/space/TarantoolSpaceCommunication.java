package io.art.tarantool.space;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.storage.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.property.Property.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import java.util.function.*;

public class TarantoolSpaceCommunication implements Communication {
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;
    private final Supplier<TarantoolClient> client;
    private final Property<TarantoolStorage> storage;
    private final LazyProperty<BiFunction<Flux<Object>, TarantoolClient, Flux<Object>>> caller = lazy(this::call);

    private String function;
    private MetaType<?> inputMappingType;
    private MetaType<?> outputMappingType;

    private final static ThreadLocal<TarantoolSpaceDecorator> decorator = new ThreadLocal<>();

    public TarantoolSpaceCommunication(Supplier<TarantoolStorage> storage, TarantoolModuleConfiguration moduleConfiguration) {
        this.storage = property(storage);
        this.writer = moduleConfiguration.getWriter();
        this.reader = moduleConfiguration.getReader();
        this.client = () -> let(decorator.get(), TarantoolSpaceDecorator::isImmutable, false)
                ? storage.get().immutable()
                : storage.get().mutable();
    }

    @Override
    public void initialize(CommunicatorAction action) {
        this.function = action.getId().getCommunicatorId() + DOT + action.getId().getActionId();
        inputMappingType = action.getInputType();
        if (nonNull(inputMappingType) && (inputMappingType.internalKind() == MONO || inputMappingType.internalKind() == FLUX)) {
            inputMappingType = inputMappingType.parameters().get(0);
        }

        outputMappingType = action.getOutputType();
        if (nonNull(outputMappingType) && (outputMappingType.internalKind() == MONO || outputMappingType.internalKind() == FLUX)) {
            outputMappingType = outputMappingType.parameters().get(0);
        }
    }

    @Override
    public void dispose() {
        if (storage.initialized()) {
            storage.get().dispose();
            storage.dispose();
        }
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return caller.get().apply(input, client.get());
    }

    private BiFunction<Flux<Object>, TarantoolClient, Flux<Object>> call() {
        if (isNull(inputMappingType)) {
            return (input, client) -> cast(client.call(function).map(element -> reader.read(outputMappingType, element)).flux());
        }

        return (input, client) -> {
            Sinks.Many<Object> emitter = Sinks.many().unicast().onBackpressureBuffer();
            subscribeInput(input, client, emitter);
            return emitter.asFlux();
        };
    }

    private void subscribeInput(Flux<Object> input, TarantoolClient client, Sinks.Many<Object> emitter) {
        input
                .doOnNext(element -> client.call(function, Mono.just(writer.write(inputMappingType, element)))
                        .doOnNext(value -> emitOutput(emitter, value))
                        .doOnError(error -> emitError(emitter, error))
                        .subscribe())
                .doOnError(error -> emitError(emitter, error))
                .subscribe();
    }

    private void emitError(Sinks.Many<Object> emitter, Throwable error) {
        emitter.tryEmitError(error);
        emitter.tryEmitComplete();
    }

    private void emitOutput(Sinks.Many<Object> emitter, org.msgpack.value.Value value) {
        emitter.tryEmitNext(reader.read(outputMappingType, value));
        emitter.tryEmitComplete();
    }

    static void decorateTarantoolSpace(UnaryOperator<TarantoolSpaceDecorator> decorator) {
        TarantoolSpaceCommunication.decorator.set(decorator.apply(new TarantoolSpaceDecorator()));
    }
}
