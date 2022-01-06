package io.art.tarantool.client;

import io.art.logging.logger.*;
import io.art.tarantool.authenticator.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.transport.*;
import io.art.tarantool.registry.*;
import io.netty.buffer.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.descriptor.TarantoolRequestWriter.*;
import static io.art.tarantool.descriptor.TarantoolResponseReader.*;
import static io.art.tarantool.factory.TarantoolRequestContentFactory.*;
import static io.netty.channel.ChannelOption.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Sinks.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class TarantoolClient {
    private final TarantoolClientConfiguration configuration;

    private volatile Disposable disposer;
    private volatile Mono<? extends Connection> connection;

    private final Sinks.One<TarantoolClient> connector = one();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean authenticated = new AtomicBoolean(false);
    private final Sinks.Many<ByteBuf> sender = many().unicast().onBackpressureBuffer();
    private final TarantoolReceiverRegistry receivers = new TarantoolReceiverRegistry(RECEIVERS_POOL_MAXIMUM);

    private final static Logger logger = logger(TarantoolClient.class);

    public Mono<TarantoolClient> connect() {
        connection = TcpClient.create()
                .host(configuration.getHost())
                .port(configuration.getPort())
                .option(CONNECT_TIMEOUT_MILLIS, (int) configuration.getConnectionTimeout().toMillis())
                .connect();

        return connector.asMono().doOnSubscribe(subscription -> subscribe());
    }

    public void dispose() {
        apply(disposer, Disposable::dispose);
    }

    public Mono<Value> call(String name) {
        return connector.asMono().flatMap(client -> client.executeCall(name));
    }

    public Mono<Value> call(String name, Mono<Value> input) {
        return connector.asMono().flatMap(client -> client.executeCall(name, input));
    }

    private Mono<Value> executeCall(String name) {
        TarantoolReceiver receiver = receivers.allocate();
        emitCall(receiver.getId(), callRequest(name));
        return receiver.getSink().asMono();
    }

    private Mono<Value> executeCall(String name, Mono<Value> argument) {
        TarantoolReceiver receiver = receivers.allocate();
        subscribeInput(name, argument, receiver);
        return receiver.getSink().asMono();
    }

    private void subscribeInput(String name, Mono<Value> arguments, TarantoolReceiver receiver) {
        arguments
                .doOnNext(argument -> emitCall(receiver.getId(), callRequest(name, argument)))
                .doOnError(logger::error)
                .subscribe();
    }

    private void emitCall(int id, Value body) {
        ByteBuf tarantoolRequest = writeTarantoolRequest(new TarantoolHeader(id, IPROTO_CALL), body);
        sender.tryEmitNext(tarantoolRequest);
    }

    private void onAuthenticate(boolean authenticated, String error) {
        if (authenticated && this.authenticated.compareAndSet(false, true)) {
            connector.tryEmitValue(this);
            return;
        }

        if (connected.compareAndSet(true, false)) {
            connector.tryEmitError(new TarantoolModuleException(error));
        }
    }

    private void receive(ByteBuf bytes) {
        TarantoolResponse response = readTarantoolResponse(bytes);
        TarantoolReceiver receiver = receivers.free(response.getHeader().getSyncId());
        if (isNull(receiver)) return;
        One<Value> sink = receiver.getSink();
        Value body = response.getBody();
        if (response.isError()) {
            sink.tryEmitError(new TarantoolModuleException(let(body, Value::toJson)));
            return;
        }
        if (isNull(body) || !body.isMapValue()) {
            sink.tryEmitEmpty();
            return;
        }
        Map<Value, Value> mapValue = body.asMapValue().map();
        Value bodyData = mapValue.get(newInteger(IPROTO_BODY_DATA));
        ArrayValue bodyValues;
        if (isNull(bodyData) || !bodyData.isArrayValue() || (bodyValues = bodyData.asArrayValue()).size() != 1) {
            sink.tryEmitEmpty();
            return;
        }
        sink.tryEmitValue(bodyValues.get(0));
    }

    private void subscribe() {
        if (connected.compareAndSet(false, true)) {
            apply(connection, mono -> disposer = mono.subscribe(this::setup));
        }
    }

    private void setup(Connection connection) {
        connection
                .addHandlerLast(new TarantoolAuthenticationRequester(configuration.getUsername(), configuration.getPassword()))
                .addHandlerLast(new TarantoolAuthenticationResponder(this::onAuthenticate));
        connection.inbound()
                .receive()
                .doOnError(logger::error)
                .filter(ignore -> authenticated.get())
                .doOnNext(this::receive)
                .subscribe();
        connection.outbound()
                .send(sender.asFlux().doOnError(logger::error))
                .then()
                .subscribe();
    }
}
