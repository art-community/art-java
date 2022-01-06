package io.art.tarantool.module.state;


import io.art.core.module.*;
import io.art.tarantool.client.*;
import io.art.tarantool.cluster.*;
import io.art.tarantool.module.connection.registry.*;
import io.art.tarantool.module.refresher.*;
import java.util.function.*;

public class TarantoolModuleState implements ModuleState {
    private final TarantoolClusterRegistry connectors;
    private final TarantoolClientRegistry clients;

    public TarantoolModuleState(TarantoolModuleRefresher.Consumer changes) {
        connectors = new TarantoolClusterRegistry(changes.clusterConsumers());
        clients = new TarantoolClientRegistry(changes.clientConsumers());
    }

    public Supplier<TarantoolConnector> getConnector(String clusterId) {
        return connectors.get(clusterId);
    }

    public Supplier<TarantoolClient> getClient(String clusterId, String clientId) {
        return clients.get(clusterId, clientId);
    }
}
