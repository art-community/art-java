package io.art.tarantool.communication;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.property.*;
import io.art.message.pack.descriptor.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;
import reactor.core.publisher.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Objects.*;
import java.util.function.*;

public class TarantoolCommunication implements Communication {
    private final MessagePackWriter writer = new MessagePackWriter();
    private final MessagePackReader reader = new MessagePackReader();
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

    @Override
    public void initialize(CommunicatorAction action) {
        this.action = action;
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
        if (isNull(action.getInputType())) {
            return (input, client) -> client
                    .call(action.getId().getActionId(), Flux.empty())
                    .map(output -> reader.read(action.getOutputType(), output));
        }

        return (input, client) -> client
                .call(action.getId().getActionId(), input.map(value -> writer.write(action.getInputType(), value)))
                .map(output -> reader.read(action.getOutputType(), output));
    }
}
