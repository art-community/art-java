package io.art.rsocket.module;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.configuration.server.RsocketHttpServerConfiguration.*;
import io.art.rsocket.configuration.server.RsocketTcpServerConfiguration.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import io.art.server.method.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

public class RsocketServerConfigurator extends ServerConfigurator {
    private boolean tcp;
    private boolean http;
    private UnaryOperator<RsocketTcpServerConfigurator> tcpConfigurator = identity();
    private UnaryOperator<RsocketHttpServerConfigurator> httpConfigurator = identity();

    public RsocketServerConfigurator tcp() {
        this.tcp = true;
        return this;
    }

    public RsocketServerConfigurator http() {
        this.http = true;
        return this;
    }

    public RsocketServerConfigurator tcp(UnaryOperator<RsocketTcpServerConfigurator> configurator) {
        this.tcp = true;
        this.tcpConfigurator = configurator;
        return this;
    }

    public RsocketServerConfigurator http(UnaryOperator<RsocketHttpServerConfigurator> configurator) {
        this.http = true;
        this.httpConfigurator = configurator;
        return this;
    }

    RsocketServerConfigurator() {
        super(lazy(() -> rsocketModule().configuration().getServer()));
    }

    RsocketTcpServerConfiguration configure(RsocketTcpServerConfiguration current) {
        RsocketTcpServerConfigurator configurator = tcpConfigurator.apply(new RsocketTcpServerConfigurator());
        RsocketTcpServerConfigurationBuilder builder = current.toBuilder();
        return configurator.tcp()
                .apply(builder)
                .common(configurator.common().apply(current.getCommon().toBuilder()).build())
                .build();
    }

    RsocketHttpServerConfiguration configure(RsocketHttpServerConfiguration current) {
        RsocketHttpServerConfigurator configurator = httpConfigurator.apply(new RsocketHttpServerConfigurator());
        RsocketHttpServerConfigurationBuilder builder = current.toBuilder();
        return configurator.http()
                .apply(builder)
                .common(configurator.common().apply(current.getCommon().toBuilder()).build())
                .build();
    }

    boolean enableTcp() {
        return tcp;
    }

    boolean enableHttp() {
        return http;
    }

    ServerConfiguration configureServer(ServerConfiguration current) {
        return create();
    }
}
