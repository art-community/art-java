package io.art.tarantool.client;

import io.art.core.extensions.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import io.art.tarantool.authenticator.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.decoder.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;
import io.art.tarantool.registry.*;
import io.netty.buffer.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.descriptor.TarantoolRequestWriter.*;
import static io.art.tarantool.factory.TarantoolRequestContentFactory.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Sinks.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class TarantoolConnection {
    private final static LazyProperty<Logger> logger = lazy(() -> logger(TARANTOOL_LOGGER));

    private final TarantoolClientConfiguration clientConfiguration;

    private final Sinks.One<TarantoolConnection> connector = one();
    private final AtomicBoolean authenticated = new AtomicBoolean(false);
    private final Sinks.Many<ByteBuf> sender = many().unicast().onBackpressureBuffer();
    private final TarantoolReceiverRegistry receivers = new TarantoolReceiverRegistry(RECEIVERS_POOL_MAXIMUM);
    private final Connection connection;

    public TarantoolConnection(TarantoolClientConfiguration clientConfiguration, Connection connection, Mono<Void> disposer) {
        this.clientConfiguration = clientConfiguration;
        this.connection = connection;

        connection
                .addHandlerLast(new TarantoolAuthenticationRequester(this.clientConfiguration.getUsername(), this.clientConfiguration.getPassword()))
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

        disposer.subscribe(ignore -> connection.disposeNow());
    }

    public Mono<TarantoolConnection> connect() {
        return connector.asMono();
    }

    public Mono<Value> executeCall(ImmutableStringValue name, Consumer<ArrayValue> onChunk) {
        TarantoolReceiver receiver = receivers.allocate(onChunk);
        emitCall(receiver.getId(), callRequest(name, newArray()));
        return receiver.getSink()
                .asMono()
                .timeout(clientConfiguration.getExecutionTimeout())
                .doOnCancel(connection::dispose)
                .doOnTerminate(connection::dispose);
    }

    public Mono<Value> executeCall(ImmutableStringValue name, Mono<Value> argument, Consumer<ArrayValue> onChunk) {
        TarantoolReceiver receiver = receivers.allocate(onChunk);
        subscribeInput(name, argument, receiver);
        return receiver.getSink()
                .asMono()
                .timeout(clientConfiguration.getExecutionTimeout())
                .doOnCancel(connection::onDispose)
                .doOnTerminate(connection::dispose);
    }

    public Mono<Value> executeCall(ImmutableStringValue name, ArrayValue arguments, Consumer<ArrayValue> onChunk) {
        TarantoolReceiver receiver = receivers.allocate(onChunk);
        emitCall(receiver.getId(), callRequest(name, arguments));
        return receiver.getSink()
                .asMono()
                .timeout(clientConfiguration.getExecutionTimeout())
                .doOnCancel(connection::onDispose)
                .doOnTerminate(connection::dispose);
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
        Sinks.One<Value> sink = receiver.getSink();
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

}
