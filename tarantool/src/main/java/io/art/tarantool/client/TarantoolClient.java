package io.art.tarantool.client;

import io.art.core.extensions.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import io.art.tarantool.authenticator.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.decoder.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;
import io.art.tarantool.registry.*;
import io.netty.buffer.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.descriptor.TarantoolRequestWriter.*;
import static io.art.tarantool.factory.TarantoolRequestContentFactory.*;
import static io.art.tarantool.service.subscription.TarantoolSubscriptionService.*;
import static io.netty.channel.ChannelOption.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Sinks.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class TarantoolClient {
    private final static LazyProperty<Logger> logger = lazy(() -> logger(TARANTOOL_LOGGER));

    private final TarantoolClientConfiguration clientConfiguration;
    private final TarantoolModuleConfiguration moduleConfiguration;

    private final Sinks.One<TarantoolClient> connector = one();
    private final AtomicBoolean connecting = new AtomicBoolean(false);
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean authenticated = new AtomicBoolean(false);
    private final Sinks.Many<ByteBuf> sender = many().unicast().onBackpressureBuffer();
    private final TarantoolReceiverRegistry receivers = new TarantoolReceiverRegistry(RECEIVERS_POOL_MAXIMUM);

    private volatile Disposable disposer;

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
        if (connected.get()) return executeCall(name, onChunk);
        return connector.asMono().flatMap(client -> client.executeCall(name, onChunk)).doOnSubscribe(ignore -> connect());
    }

    public Mono<Value> call(ImmutableStringValue name, Mono<Value> input, Consumer<ArrayValue> onChunk) {
        if (connected.get()) return executeCall(name, input, onChunk);
        return connector.asMono().flatMap(client -> client.executeCall(name, input, onChunk)).doOnSubscribe(ignore -> connect());
    }

    public Mono<Value> call(ImmutableStringValue name, ArrayValue arguments, Consumer<ArrayValue> onChunk) {
        if (connected.get()) return executeCall(name, arguments, onChunk);
        return connector.asMono().flatMap(client -> client.executeCall(name, arguments, onChunk)).doOnSubscribe(ignore -> connect());
    }

    public void dispose() {
        apply(disposer, Disposable::dispose);
    }

    private Mono<Value> executeCall(ImmutableStringValue name, Consumer<ArrayValue> onChunk) {
        TarantoolReceiver receiver = receivers.allocate(onChunk);
        emitCall(receiver.getId(), callRequest(name, newArray()));
        return receiver.getSink().asMono().timeout(clientConfiguration.getExecutionTimeout());
    }

    private Mono<Value> executeCall(ImmutableStringValue name, Mono<Value> argument, Consumer<ArrayValue> onChunk) {
        TarantoolReceiver receiver = receivers.allocate(onChunk);
        subscribeInput(name, argument, receiver);
        return receiver.getSink().asMono().timeout(clientConfiguration.getExecutionTimeout());
    }

    private Mono<Value> executeCall(ImmutableStringValue name, ArrayValue arguments, Consumer<ArrayValue> onChunk) {
        TarantoolReceiver receiver = receivers.allocate(onChunk);
        emitCall(receiver.getId(), callRequest(name, arguments));
        return receiver.getSink().asMono().timeout(clientConfiguration.getExecutionTimeout());
    }

    private void subscribeInput(ImmutableStringValue name, Mono<Value> argument, TarantoolReceiver receiver) {
        argument
                .doOnNext(next -> emitCall(receiver.getId(), callRequest(name, newArray(next))))
                .doOnError(error -> withLogging(() -> logger.get().error(error)))
                .subscribe();
    }

    private void emitCall(IntegerValue id, Value body) {
        sender.tryEmitNext(writeTarantoolRequest(new TarantoolHeader(id, IPROTO_CALL), body));
    }

    private void onAuthenticate(boolean authenticated, String error) {
        if (authenticated && this.authenticated.compareAndSet(false, true)) {
            connector.tryEmitValue(this);
        }
    }

    private void receive(TarantoolResponse response) {
        int id = response.getHeader().getSyncId().asInt();
        TarantoolReceiver receiver = receivers.get(id);
        if (isNull(receiver)) return;
        One<Value> sink = receiver.getSink();
        Value body = response.getBody();
        if (response.isError()) {
            receivers.free(id);
            if (isNull(body) || !body.isMapValue()) {
                sink.tryEmitError(new TarantoolException(let(body, Value::toJson)));
                return;
            }
            Map<Value, Value> mapValue = body.asMapValue().map();
            sink.tryEmitError(new TarantoolException(let(mapValue.get(IPROTO_ERROR), Value::toJson)));
            return;
        }
        if (response.isChunk()) {
            if (isNull(body) || !body.isMapValue()) {
                return;
            }
            Map<Value, Value> mapValue = body.asMapValue().map();
            Value bodyData = mapValue.get(IPROTO_BODY_DATA);
            ArrayValue bodyValues;
            if (isNull(bodyData) || !bodyData.isArrayValue() || (bodyValues = bodyData.asArrayValue()).size() != 1) {
                return;
            }
            receiver.getOnChunk().accept(bodyValues);
            return;
        }
        receivers.free(id);
        if (isNull(body) || !body.isMapValue()) {
            sink.tryEmitEmpty();
            return;
        }
        Map<Value, Value> mapValue = body.asMapValue().map();
        Value bodyData = mapValue.get(IPROTO_BODY_DATA);
        ArrayValue bodyValues;
        if (isNull(bodyData) || !bodyData.isArrayValue() || (bodyValues = bodyData.asArrayValue()).size() != 1) {
            sink.tryEmitEmpty();
            return;
        }
        sink.tryEmitValue(bodyValues.get(0));
    }

    private void connect() {
        if (connecting.compareAndSet(false, true)) {
            disposer = TcpClient.create()
                    .host(clientConfiguration.getHost())
                    .port(clientConfiguration.getPort())
                    .option(CONNECT_TIMEOUT_MILLIS, (int) clientConfiguration.getConnectionTimeout().toMillis())
                    .connect()
                    .timeout(clientConfiguration.getConnectionTimeout())
                    .subscribe(this::setup);
        }
    }

    private void setup(Connection connection) {
        if (connected.compareAndSet(false, true)) {
            connection.markPersistent(true);
            connection
                    .addHandlerLast(new TarantoolAuthenticationRequester(clientConfiguration.getUsername(), clientConfiguration.getPassword()))
                    .addHandlerLast(new TarantoolResponseDecoder())
                    .addHandlerLast(new TarantoolAuthenticationResponder(this::onAuthenticate));
            connection.inbound()
                    .receiveObject()
                    .doOnNext(object -> receive(cast(object)))
                    .doOnError(error -> withLogging(() -> logger.get().error(error)))
                    .subscribe();
            connection.outbound()
                    .send(sender.asFlux()
                            .doOnError(error -> withLogging(() -> logger.get().error(error)))
                            .doOnDiscard(ByteBuf.class, NettyBufferExtensions::releaseBuffer))
                    .then()
                    .subscribe();
        }
    }
}
