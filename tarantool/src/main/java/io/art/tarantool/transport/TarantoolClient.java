package io.art.tarantool.transport;

import io.art.logging.logger.*;
import io.art.tarantool.authenticator.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;
import io.netty.buffer.*;
import io.netty.util.collection.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.descriptor.TarantoolRequestWriter.*;
import static io.art.tarantool.descriptor.TarantoolResponseReader.*;
import static io.art.tarantool.state.TarantoolRequestIdState.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Sinks.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class TarantoolClient {
    private NettyInbound inbound;
    private NettyOutbound outbound;
    private volatile Disposable disposer;
    private final Sinks.One<TarantoolClient> connector = one();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean authenticated = new AtomicBoolean(false);
    private final TarantoolInstanceConfiguration configuration;
    private final IntObjectMap<Sinks.One<Value>> receivers = new IntObjectHashMap<>(128);

    private final static Logger logger = logger(TarantoolClient.class);

    public Mono<TarantoolClient> connect() {
        disposer = TcpClient.create()
                .host(configuration.getHost())
                .port(configuration.getPort())
                .connect()
                .subscribe(this::setup);
        return connector.asMono();
    }

    public void dispose() {
        apply(disposer, Disposable::dispose);
    }

    @SuppressWarnings(CALLING_SUBSCRIBE_IN_NON_BLOCKING_SCOPE)
    public Mono<Value> send(Mono<Value> input) {
        int id = nextId();
        One<Value> receiver = one();
        receivers.put(id, receiver);
        outbound.send(input.map(value -> writeTarantoolRequest(new TarantoolHeader(id, IPROTO_CALL), value)))
                .then()
                .subscribe();
        return receiver.asMono();
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
            if (isNull(receiver)) {
                return;
            }
            if (response.isError()) {
                receiver.tryEmitError(new TarantoolModuleException(let(response.getBody(), Value::toJson)));
                return;
            }
            if (isNull(response.getBody())) {
                receiver.tryEmitEmpty();
                return;
            }
            receiver.tryEmitValue(response.getBody());
        }
    }

    private void setup(Connection connection) {
        if (connected.compareAndSet(false, true)) {
            this.inbound = connection.inbound();
            this.outbound = connection.outbound();
            connection
                    .addHandlerLast(new TarantoolAuthenticationRequester(configuration.getUsername(), configuration.getPassword()))
                    .addHandlerLast(new TarantoolAuthenticationResponder(this::onAuthenticate));
            inbound.receive().doOnError(logger::error).doOnNext(this::receive).subscribe();
        }
    }

}
