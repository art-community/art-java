package io.art.tarantool.communication;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.descriptor.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import java.util.function.*;

public class TarantoolCommunication implements Communication {
    private final TarantoolModelWriter writer = new TarantoolModelWriter();
    private final TarantoolModelReader reader = new TarantoolModelReader();
    private CommunicatorAction action;
    private final TarantoolClient client = new TarantoolClient(TarantoolInstanceConfiguration.builder()
            .host("localhost")
            .port(3301)
            .username("username")
            .password("password")
            .connectionTimeout(30)
            .build());
    private final Mono<TarantoolClient> connectedClient = client.connect();
    private final LazyProperty<BiFunction<Flux<Object>, TarantoolClient, Flux<Object>>> caller = lazy(this::call);
    private MetaType<?> inputMappingType;
    private MetaType<?> outputMappingType;

    @Override
    public void initialize(CommunicatorAction action) {
        this.action = action;
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
        client.dispose();
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return connectedClient.flatMapMany(client -> caller.get().apply(input, client));
    }

    private BiFunction<Flux<Object>, TarantoolClient, Flux<Object>> call() {
        String actionId = action.getId().getActionId();

        if (isNull(inputMappingType)) {
            return (input, client) -> cast(client.call(actionId).map(element -> reader.read(outputMappingType, element)).flux());
        }

        return (input, client) -> {
            Sinks.Many<Object> emitter = Sinks.many().unicast().onBackpressureBuffer();
            subscribeInput(actionId, input, client, emitter);
            return emitter.asFlux();
        };
    }

    private void subscribeInput(String actionId, Flux<Object> input, TarantoolClient client, Sinks.Many<Object> emitter) {
        input
                .doOnNext(element -> client.call(actionId, Mono.just(writer.write(inputMappingType, element)))
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
}
