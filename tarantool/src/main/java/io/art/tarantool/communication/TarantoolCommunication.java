package io.art.tarantool.communication;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.message.pack.descriptor.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.transport.*;
import reactor.core.publisher.*;
import static io.art.tarantool.factory.TarantoolRequestContentFactory.*;
import static java.util.Objects.*;

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
    private Mono<TarantoolClient> connectedClient = Mono.never();

    @Override
    public void initialize(CommunicatorAction action) {
        this.action = action;
        connectedClient = client.connect();
    }

    @Override
    public void dispose() {
        client.dispose();
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return connectedClient.flatMapMany(client -> isNull(action.getInputType())

                ? client
                .send(Flux.just(callRequest(action.getId().getActionId())))
                .map(output -> reader.read(action.getOutputType(), output))

                : client
                .send(input.map(element -> callRequest(action.getId().getActionId(), writer.write(action.getInputType(), element))))
                .map(output -> reader.read(action.getOutputType(), output)));

    }
}
