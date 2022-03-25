package io.art.tarantool.communicator;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.descriptor.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.function.*;

public class TarantoolCommunication implements Communication {
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;
    private final Supplier<TarantoolClient> client;
    private final TarantoolStorageConnector clients;
    private final LazyProperty<BiFunction<Flux<Object>, TarantoolClient, Flux<Object>>> caller = lazy(this::call);

    private ImmutableStringValue function;
    private MetaType<?> inputMappingType;
    private MetaType<?> outputMappingType;

    private final static ThreadLocal<TarantoolCommunicationDecorator> decorator = new ThreadLocal<>();

    public TarantoolCommunication(TarantoolStorageConnector connector, TarantoolModuleConfiguration moduleConfiguration) {
        this.clients = connector;
        this.writer = moduleConfiguration.getWriter();
        this.reader = moduleConfiguration.getReader();
        this.client = () -> connector.hasRouters() ? connector.router() : let(decorator.get(), TarantoolCommunicationDecorator::isImmutable, false)
                ? connector.immutable()
                : connector.mutable();
    }

    @Override
    public void initialize(CommunicatorAction action) {
        this.function = newString(action.getId().getActionId());
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
        clients.dispose();
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return caller.get().apply(input, client.get());
    }

    private BiFunction<Flux<Object>, TarantoolClient, Flux<Object>> call() {

        if (isNull(inputMappingType)) {
            return (input, client) -> {
                TarantoolCommunicationDecorator decorator = TarantoolCommunication.decorator.get();
                TarantoolCommunication.decorator.remove();

                if (nonNull(decorator) && decorator.isChannel()) {
                    Sinks.Many<Object> emitter = Sinks.many().unicast().onBackpressureBuffer();
                    subscribeChannelEmptyInput(client, emitter);
                    return emitter.asFlux();
                }
                return cast(client.call(function).map(element -> reader.read(outputMappingType, element)).flux());
            };
        }

        return (input, client) -> {
            TarantoolCommunicationDecorator decorator = TarantoolCommunication.decorator.get();
            TarantoolCommunication.decorator.remove();

            if (nonNull(decorator) && decorator.isChannel()) {
                Sinks.Many<Object> emitter = Sinks.many().unicast().onBackpressureBuffer();
                subscribeChannelInput(input, client, emitter);
                return emitter.asFlux();
            }

            Sinks.Many<Object> emitter = Sinks.many().unicast().onBackpressureBuffer();
            subscribeFunction(input, client, emitter);
            return emitter.asFlux();
        };
    }

    private void subscribeChannelEmptyInput(TarantoolClient client, Sinks.Many<Object> emitter) {
        client.call(function, value -> emitChannelOutput(emitter, value))
                .doOnError(error -> emitError(emitter, error))
                .subscribe();
    }

    private void subscribeChannelInput(Flux<Object> input, TarantoolClient client, Sinks.Many<Object> emitter) {
        input
                .doOnNext(element -> client.call(function, Mono.just(writer.write(inputMappingType, element)), value -> emitChannelOutput(emitter, value))
                        .doOnError(error -> emitError(emitter, error))
                        .subscribe())
                .doOnError(error -> emitError(emitter, error))
                .subscribe();
    }

    private void subscribeFunction(Flux<Object> input, TarantoolClient client, Sinks.Many<Object> emitter) {
        input
                .doOnNext(element -> client.call(function, Mono.just(writer.write(inputMappingType, element)))
                        .doOnNext(value -> emitFunctionOutput(emitter, value))
                        .doOnError(error -> emitError(emitter, error))
                        .subscribe())
                .doOnError(error -> emitError(emitter, error))
                .subscribe();
    }

    private void emitError(Sinks.Many<Object> emitter, Throwable error) {
        emitter.tryEmitError(error);
        emitter.tryEmitComplete();
    }

    private void emitFunctionOutput(Sinks.Many<Object> emitter, org.msgpack.value.Value value) {
        emitter.tryEmitNext(reader.read(outputMappingType, value));
        emitter.tryEmitComplete();
    }

    private void emitChannelOutput(Sinks.Many<Object> emitter, org.msgpack.value.ArrayValue value) {
        if (isNull(value) || value.size() == 0) return;
        emitter.tryEmitNext(reader.read(outputMappingType, value.get(0)));
    }

    static void decorateTarantoolCommunication(UnaryOperator<TarantoolCommunicationDecorator> decorator) {
        TarantoolCommunication.decorator.set(decorator.apply(orElse(TarantoolCommunication.decorator.get(), new TarantoolCommunicationDecorator())));
    }
}
