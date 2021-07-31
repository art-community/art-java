package io.art.rsocket.module;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.RsocketHttpServerConfiguration.*;
import io.art.rsocket.configuration.RsocketTcpServerConfiguration.*;
import io.art.server.configurator.*;
import io.art.server.method.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
public class RsocketServerConfigurator extends ServerConfigurator {
    private boolean tcp;
    private boolean http;

    private UnaryOperator<RsocketTcpServerConfigurationBuilder> tcpConfigurator = UnaryOperator.identity();
    private UnaryOperator<RsocketHttpServerConfigurationBuilder> httpConfigurator = UnaryOperator.identity();

    public RsocketServerConfigurator tcp() {
        this.tcp = true;
        return this;
    }

    public RsocketServerConfigurator http() {
        this.http = true;
        return this;
    }

    public RsocketServerConfigurator tcp(UnaryOperator<RsocketTcpServerConfigurationBuilder> configurator) {
        this.tcp = true;
        this.tcpConfigurator = configurator;
        return this;
    }

    public RsocketServerConfigurator http(UnaryOperator<RsocketHttpServerConfigurationBuilder> configurator) {
        this.http = true;
        this.httpConfigurator = configurator;
        return this;
    }

    RsocketServerConfigurator() {
        super(() -> rsocketModule().configuration().getServerConfiguration());
    }

    RsocketTcpServerConfiguration configure(RsocketTcpServerConfiguration current) {
        return tcpConfigurator.apply(current.toBuilder()).build();
    }

    RsocketHttpServerConfiguration configure(RsocketHttpServerConfiguration current) {
        return httpConfigurator.apply(current.toBuilder()).build();
    }

    boolean enableTcp() {
        return tcp;
    }

    boolean enableHttp() {
        return http;
    }

    LazyProperty<ImmutableArray<ServiceMethod>> serviceMethods() {
        return get();
    }
}
