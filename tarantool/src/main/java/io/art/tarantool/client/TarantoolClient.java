package io.art.tarantool.client;

import io.art.tarantool.configuration.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.registry.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import reactor.netty.tcp.*;
import static io.art.tarantool.service.subscription.TarantoolSubscriptionService.*;
import static io.netty.channel.ChannelOption.*;
import java.util.function.*;

public class TarantoolClient {
    private final TarantoolClientConfiguration clientConfiguration;
    private final TarantoolModuleConfiguration moduleConfiguration;
    private final TcpClient client;
    private final Sinks.One<Void> disposer = Sinks.one();

    public TarantoolClient(TarantoolClientConfiguration clientConfiguration, TarantoolModuleConfiguration moduleConfiguration) {
        this.clientConfiguration = clientConfiguration;
        this.moduleConfiguration = moduleConfiguration;
        client = TcpClient.create()
                .host(clientConfiguration.getHost())
                .port(clientConfiguration.getPort())
                .option(CONNECT_TIMEOUT_MILLIS, (int) clientConfiguration.getConnectionTimeout().toMillis());
    }

    public Mono<Value> call(ImmutableStringValue name) {
        TarantoolModelReader reader = moduleConfiguration.getReader();
        TarantoolSubscriptionRegistry subscriptions = moduleConfiguration.getSubscriptions();
        return call(name, payload -> publish(payload, subscriptions, reader));
    }

    public Mono<Value> call(ImmutableStringValue name, Mono<Value> input) {
        TarantoolModelReader reader = moduleConfiguration.getReader();
        TarantoolSubscriptionRegistry subscriptions = moduleConfiguration.getSubscriptions();
        return call(name, input, payload -> publish(payload, subscriptions, reader));
    }

    public Mono<Value> call(ImmutableStringValue name, ArrayValue arguments) {
        TarantoolModelReader reader = moduleConfiguration.getReader();
        TarantoolSubscriptionRegistry subscriptions = moduleConfiguration.getSubscriptions();
        return call(name, arguments, payload -> publish(payload, subscriptions, reader));
    }

    public Mono<Value> call(ImmutableStringValue name, Consumer<ArrayValue> onChunk) {
        return connect().flatMap(connection -> connection.executeCall(name, onChunk));
    }

    public Mono<Value> call(ImmutableStringValue name, Mono<Value> input, Consumer<ArrayValue> onChunk) {
        return connect().flatMap(connection -> connection.executeCall(name, input, onChunk));
    }

    public Mono<Value> call(ImmutableStringValue name, ArrayValue arguments, Consumer<ArrayValue> onChunk) {
        return connect().flatMap(connection -> connection.executeCall(name, arguments, onChunk));
    }

    public void dispose() {
        disposer.tryEmitEmpty();
    }

    private Mono<TarantoolConnection> connect() {
        return client.connect()
                .retry(clientConfiguration.getRetryCount())
                .timeout(clientConfiguration.getConnectionTimeout())
                .flatMap(connection -> new TarantoolConnection(clientConfiguration, connection, disposer.asMono()).connect());
    }
}
