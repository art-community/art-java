package io.art.rsocket.module;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.RsocketHttpServerConfiguration.*;
import io.art.rsocket.configuration.RsocketTcpServerConfiguration.*;
import io.art.server.method.*;
import io.art.server.registrator.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
public class RsocketServerConfigurator extends ServerConfigurator {
    private boolean tcp;
    private boolean http;

    private UnaryOperator<RsocketTcpServerConfigurationBuilder> tcpDecorator = UnaryOperator.identity();
    private UnaryOperator<RsocketHttpServerConfigurationBuilder> httpDecorator = UnaryOperator.identity();

    public RsocketServerConfigurator tcp() {
        this.tcp = true;
        return this;
    }

    public RsocketServerConfigurator http() {
        this.http = true;
        return this;
    }

    public RsocketServerConfigurator tcp(UnaryOperator<RsocketTcpServerConfigurationBuilder> decorator) {
        this.tcp = true;
        this.tcpDecorator = decorator;
        return this;
    }

    public RsocketServerConfigurator http(UnaryOperator<RsocketHttpServerConfigurationBuilder> decorator) {
        this.http = true;
        this.httpDecorator = decorator;
        return this;
    }

    RsocketServerConfigurator() {
        super(() -> rsocketModule().configuration().getServerConfiguration());
    }

    RsocketTcpServerConfiguration configure(RsocketTcpServerConfiguration current) {
        return tcpDecorator.apply(current.toBuilder()).build();
    }

    RsocketHttpServerConfiguration configure(RsocketHttpServerConfiguration current) {
        return httpDecorator.apply(current.toBuilder()).build();
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
