package io.art.tarantool.client;

import io.art.core.property.*;
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
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.property.LazyProperty.*;
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
    private final static LazyProperty<Logger> logger = lazy(() -> logger(TARANTOOL_LOGGER));

    private final TarantoolClientConfiguration configuration;

    private final Sinks.One<TarantoolClient> connector = one();
    private final AtomicBoolean connecting = new AtomicBoolean(false);
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean authenticated = new AtomicBoolean(false);
    private final Sinks.Many<ByteBuf> sender = many().unicast().onBackpressureBuffer();
    private final TarantoolReceiverRegistry receivers = new TarantoolReceiverRegistry(RECEIVERS_POOL_MAXIMUM);

    private volatile Disposable disposer;

    public Mono<Value> call(String name) {
        return connector.asMono().flatMap(client -> client.executeCall(name)).doOnSubscribe(ignore -> connect());
    }

    public Mono<Value> call(String name, Mono<Value> input) {
        return connector.asMono().flatMap(client -> client.executeCall(name, input)).doOnSubscribe(ignore -> connect());
    }

    public Mono<Value> call(String name, Value... arguments) {
        return connector.asMono().flatMap(client -> client.executeCall(name, arguments)).doOnSubscribe(ignore -> connect());
    }

    public void dispose() {
        apply(disposer, Disposable::dispose);
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

    private Mono<Value> executeCall(String name, Value... arguments) {
        TarantoolReceiver receiver = receivers.allocate();
        emitCall(receiver.getId(), callRequest(name, arguments));
        return receiver.getSink().asMono();
    }

    private void subscribeInput(String name, Mono<Value> argument, TarantoolReceiver receiver) {
        argument
                .doOnNext(next -> emitCall(receiver.getId(), callRequest(name, next)))
                .doOnError(error -> withLogging(() -> logger.get().error(error)))
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
            connector.tryEmitError(new TarantoolException(error));
        }
    }

    private void receive(ByteBuf bytes) {
        TarantoolResponse response = readTarantoolResponse(bytes);
        TarantoolReceiver receiver = receivers.free(response.getHeader().getSyncId());
        if (isNull(receiver)) return;
        One<Value> sink = receiver.getSink();
        Value body = response.getBody();
        if (response.isError()) {
            sink.tryEmitError(new TarantoolException(let(body, Value::toJson)));
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

    private void connect() {
        if (connecting.compareAndSet(false, true)) {
            disposer = TcpClient.create()
                    .host(configuration.getHost())
                    .port(configuration.getPort())
                    .option(CONNECT_TIMEOUT_MILLIS, (int) configuration.getConnectionTimeout().toMillis())
                    .connect()
                    .subscribe(this::setup);
        }
    }

    private void setup(Connection connection) {
        if (connected.compareAndSet(false, true)) {
            connection.markPersistent(true);
            connection
                    .addHandlerLast(new TarantoolAuthenticationRequester(configuration.getUsername(), configuration.getPassword()))
                    .addHandlerLast(new TarantoolAuthenticationResponder(this::onAuthenticate));
            connection.inbound()
                    .receive()
                    .doOnError(error -> withLogging(() -> logger.get().error(error)))
                    .doOnNext(this::receive)
                    .subscribe();
            connection.outbound()
                    .send(sender.asFlux().doOnError(error -> withLogging(() -> logger.get().error(error))))
                    .then()
                    .subscribe();
        }
    }
}
