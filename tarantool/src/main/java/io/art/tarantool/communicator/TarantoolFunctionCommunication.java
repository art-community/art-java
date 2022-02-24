package io.art.tarantool.communicator;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.storage.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.function.*;

public class TarantoolFunctionCommunication implements Communication {
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;
    private final Supplier<TarantoolClient> client;
    private final TarantoolStorage storage;
    private final LazyProperty<BiFunction<Flux<Object>, TarantoolClient, Flux<Object>>> caller = lazy(this::call);

    private ImmutableStringValue function;
    private MetaType<?> inputMappingType;
    private MetaType<?> outputMappingType;

    private final static ThreadLocal<TarantoolCommunicationDecorator> decorator = new ThreadLocal<>();

    public TarantoolFunctionCommunication(TarantoolStorage storage, TarantoolModuleConfiguration moduleConfiguration) {
        this.storage = storage;
        this.writer = moduleConfiguration.getWriter();
        this.reader = moduleConfiguration.getReader();
        this.client = () -> let(decorator.get(), TarantoolCommunicationDecorator::isImmutable, false)
                ? storage.immutable()
                : storage.mutable();
    }

    @Override
    public void initialize(CommunicatorAction action) {
        this.function = newString(action.getId().getCommunicatorId() + DOT + action.getId().getActionId());
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
        storage.dispose();
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

    static void decorateTarantoolCommunication(UnaryOperator<TarantoolCommunicationDecorator> decorator) {
        TarantoolFunctionCommunication.decorator.set(decorator.apply(new TarantoolCommunicationDecorator()));
    }
}
