package io.art.rsocket.module;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.RsocketServerConfiguration.*;
import io.art.server.method.*;
import io.art.server.registrator.*;
import lombok.*;
import lombok.experimental.*;
import reactor.netty.http.server.*;
import reactor.netty.tcp.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
public class RsocketServerConfigurator extends ServerConfigurator {
    private UnaryOperator<TcpServer> tcpServer = UnaryOperator.identity();
    private UnaryOperator<HttpServer> httpWebSocketServer = UnaryOperator.identity();
    private UnaryOperator<RsocketServerConfigurationBuilder> decorator;

    public RsocketServerConfigurator tcp(UnaryOperator<TcpServer> decorator) {
        this.tcpServer = decorator;
        return this;
    }

    public RsocketServerConfigurator http(UnaryOperator<HttpServer> decorator) {
        this.httpWebSocketServer = decorator;
        return this;
    }

    public RsocketServerConfigurator configure(UnaryOperator<RsocketServerConfigurationBuilder> decorator) {
        this.decorator = decorator;
        return this;
    }

    RsocketServerConfigurator() {
        super(() -> rsocketModule().configuration().getServerConfiguration());
    }


    RsocketServerConfiguration configure(RsocketServerConfiguration current) {
        return decorator
                .apply(current.toBuilder()
                        .tcpServer(tcpServer.apply(current.getTcpServer()))
                        .httpWebSocketServer(httpWebSocketServer.apply(current.getHttpWebSocketServer())))
                .build();
    }

    LazyProperty<ImmutableArray<ServiceMethod>> serviceMethods() {
        return get();
    }
}
