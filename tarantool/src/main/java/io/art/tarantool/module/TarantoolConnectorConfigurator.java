package io.art.tarantool.module;

import io.art.core.annotation.*;
import io.art.tarantool.configuration.*;
import lombok.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.tarantool.configuration.TarantoolClientConfiguration.*;
import static io.art.tarantool.configuration.TarantoolConnectorConfiguration.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class TarantoolConnectorConfigurator {
    private final String connector;
    private final Set<TarantoolClientConfiguration> clients = set();
    private boolean logging = false;

    public TarantoolConnectorConfigurator client(UnaryOperator<TarantoolClientConfigurationBuilder> configurator) {
        clients.add(configurator.apply(tarantoolClientConfiguration().toBuilder()).build());
        return this;
    }

    public TarantoolConnectorConfigurator logging() {
        return logging(true);
    }

    public TarantoolConnectorConfigurator logging(boolean logging) {
        this.logging = logging;
        return this;
    }

    TarantoolConnectorConfiguration configure() {
        return tarantoolConnectorConfiguration(connector)
                .toBuilder()
                .clients(immutableSetOf(clients))
                .logging(logging)
                .build();
    }
}
