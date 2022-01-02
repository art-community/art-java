package io.art.tarantool.transport;

import io.art.logging.logger.*;
import io.art.tarantool.authenticator.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import lombok.*;
import org.msgpack.value.Value;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.descriptor.TarantoolRequestWriter.*;
import static io.art.tarantool.state.TarantoolRequestIdState.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class TarantoolClient {
    private NettyInbound inbound;
    private NettyOutbound outbound;
    private volatile Disposable disposer;
    private final Sinks.One<TarantoolClient> connector = Sinks.one();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final TarantoolInstanceConfiguration configuration;

    private final static Logger logger = logger(TarantoolClient.class);

    public Mono<TarantoolClient> connect() {
        disposer = TcpClient.create()
                .host(configuration.getHost())
                .port(configuration.getPort())
                .connect()
                .subscribe(this::handle);
        return connector.asMono();
    }

    public void dispose() {
        apply(disposer, Disposable::dispose);
    }

    public void handle(Connection connection) {
        if (connected.compareAndSet(false, true)) {
            this.inbound = connection.inbound();
            this.outbound = connection.outbound();
            connection
                    .addHandlerLast(new TarantoolAuthenticationRequester(configuration.getUsername(), configuration.getPassword()))
                    .addHandlerLast(new TarantoolAuthenticationResponder(this::onAuthenticate));
        }
    }

    public void send(Mono<Value> input) {
        outbound.send(input.map(value -> writeTarantoolRequest(nextId(), IPROTO_CALL, value))).then().subscribe();
    }

    private void onAuthenticate(boolean authenticated, String error) {
        if (authenticated) {
            connector.tryEmitValue(this);
            return;
        }
        if (connected.compareAndSet(true, false)) {
            connector.tryEmitError(new TarantoolModuleException(error));
        }
    }
}
