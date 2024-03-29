package io.art.tarantool.module;

import io.art.core.annotation.*;
import io.art.tarantool.configuration.*;
import lombok.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.tarantool.configuration.TarantoolClientConfiguration.*;
import static io.art.tarantool.configuration.TarantoolStorageConfiguration.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class TarantoolStorageConnectorConfigurator {
    private final String storageId;
    private final Set<TarantoolClientConfiguration> clients = set();
    private boolean logging = false;

    public TarantoolStorageConnectorConfigurator client(UnaryOperator<TarantoolClientConfigurationBuilder> configurator) {
        clients.add(configurator.apply(tarantoolClientConfiguration().toBuilder()).build());
        return this;
    }

    public TarantoolStorageConnectorConfigurator logging() {
        return logging(true);
    }

    public TarantoolStorageConnectorConfigurator logging(boolean logging) {
        this.logging = logging;
        return this;
    }

    TarantoolStorageConfiguration configure() {
        return tarantoolStorageConfiguration(storageId)
                .toBuilder()
                .clients(immutableSetOf(clients))
                .logging(logging)
                .build();
    }
}
