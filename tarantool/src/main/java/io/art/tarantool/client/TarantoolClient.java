package io.art.tarantool.client;

import io.art.logging.logger.*;
import io.art.tarantool.authenticator.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.transport.*;
import io.netty.buffer.*;
import io.netty.util.collection.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.descriptor.TarantoolRequestWriter.*;
import static io.art.tarantool.descriptor.TarantoolResponseReader.*;
import static io.art.tarantool.factory.TarantoolRequestContentFactory.*;
import static io.art.tarantool.state.TarantoolRequestIdState.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Sinks.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class TarantoolClient {
    private final TarantoolInstanceConfiguration configuration;

    private volatile Disposable disposer;
    private volatile Mono<? extends Connection> connection;

    private final Sinks.One<TarantoolClient> connector = one();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean authenticated = new AtomicBoolean(false);
    private final IntObjectMap<Many<Value>> receivers = new IntObjectHashMap<>(RECEIVERS_INITIAL_SIZE);
    private final Sinks.Many<ByteBuf> sender = many().unicast().onBackpressureBuffer();

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
    public Flux<Value> call(String name, Flux<Value> arguments) {
        int id = nextId();
        Many<Value> receiver = many().unicast().onBackpressureBuffer();
        receivers.put(id, receiver);
        arguments
                .doOnNext(argument -> emitCall(id, callRequest(name, argument)))
                .doOnError(logger::error)
                .subscribe();
        return receiver.asFlux();
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
        Many<Value> receiver = receivers.remove(response.getHeader().getSyncId());
        decrementId();
        if (isNull(receiver)) return;
        Value body = response.getBody();
        if (response.isError()) {
            receiver.tryEmitError(new TarantoolModuleException(let(body, Value::toJson)));
            receiver.tryEmitComplete();
            return;
        }
        if (isNull(body) || !body.isMapValue()) {
            receiver.tryEmitComplete();
            return;
        }
        Map<Value, Value> mapValue = body.asMapValue().map();
        Value bodyData = mapValue.get(newInteger(IPROTO_BODY_DATA));
        ArrayValue bodyValues;
        if (isNull(bodyData) || !bodyData.isArrayValue() || (bodyValues = bodyData.asArrayValue()).size() == 0) {
            receiver.tryEmitComplete();
            return;
        }
        receiver.tryEmitNext(bodyValues.get(0));
        receiver.tryEmitComplete();
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
