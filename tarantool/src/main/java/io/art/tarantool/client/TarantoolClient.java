package io.art.tarantool.client;

import io.art.logging.logger.*;
import io.art.tarantool.authenticator.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.transport.*;
import io.netty.buffer.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.descriptor.TarantoolRequestWriter.*;
import static io.art.tarantool.descriptor.TarantoolResponseReader.*;
import static io.art.tarantool.state.TarantoolRequestIdState.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Sinks.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class TarantoolClient {
    private final TarantoolInstanceConfiguration configuration;

    private volatile NettyOutbound outbound;
    private volatile Disposable disposer;
    private volatile Mono<? extends Connection> connection;

    private final Sinks.One<TarantoolClient> connector = one();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean authenticated = new AtomicBoolean(false);
    private final List<Sinks.One<Value>> receivers = dynamicArray(RECEIVERS_INITIAL_SIZE);

    private final static Logger logger = logger(TarantoolClient.class);

    public Mono<TarantoolClient> connect() {
        connection = TcpClient.create()
                .host(configuration.getHost())
                .port(configuration.getPort())
                .connect();
        return connector.asMono().doOnSubscribe(subscription -> subscribe());
    }

    public void dispose() {
        apply(disposer, Disposable::dispose);
    }

    @SuppressWarnings(CALLING_SUBSCRIBE_IN_NON_BLOCKING_SCOPE)
    public Flux<Value> send(Flux<Value> input) {
        int id = nextId();
        One<Value> receiver = one();
        receivers.add(id, receiver);
        Flux<ByteBuf> request = input
                .map(value -> writeTarantoolRequest(new TarantoolHeader(id, IPROTO_CALL), value))
                .switchIfEmpty(Flux.just(writeTarantoolRequest(new TarantoolHeader(id, IPROTO_CALL), newMap(emptyMap()))));
        outbound.send(request).then().subscribe();
        return receiver.asMono().flux();
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
        if (authenticated.get()) {
            TarantoolResponse response = readTarantoolResponse(bytes);
            Sinks.One<Value> receiver = receivers.remove(response.getHeader().getSyncId());
            decrementId();
            if (isNull(receiver)) return;
            Value body = response.getBody();
            if (response.isError()) {
                receiver.tryEmitError(new TarantoolModuleException(let(body, Value::toJson)));
                return;
            }
            if (isNull(body) || !body.isMapValue()) {
                receiver.tryEmitEmpty();
                return;
            }
            Map<Value, Value> mapValue = body.asMapValue().map();
            Value bodyData = mapValue.get(newInteger(IPROTO_BODY_DATA));
            if (isNull(bodyData)) {
                receiver.tryEmitEmpty();
                return;
            }
            receiver.tryEmitValue(bodyData.asArrayValue().get(0));
        }
    }

    private void subscribe() {
        if (connected.compareAndSet(false, true)) {
            apply(connection, mono -> disposer = mono.subscribe(this::setup));
        }
    }

    private void setup(Connection connection) {
        this.outbound = connection.outbound();
        connection
                .addHandlerLast(new TarantoolAuthenticationRequester(configuration.getUsername(), configuration.getPassword()))
                .addHandlerLast(new TarantoolAuthenticationResponder(this::onAuthenticate));
        connection.inbound().receive().doOnError(logger::error).doOnNext(this::receive).subscribe();
    }
}
