package io.art.tarantool.module.state;


import io.art.core.module.*;
import io.art.tarantool.module.connection.client.*;
import io.art.tarantool.module.connection.registry.*;
import io.art.tarantool.module.refresher.*;
import io.tarantool.driver.api.*;

import java.util.function.*;

public class TarantoolModuleState implements ModuleState {
    private final TarantoolClusterRegistry clusters;
    private final TarantoolClientRegistry clients;

    public TarantoolModuleState(TarantoolModuleRefresher.Consumer changes){
        clusters = new TarantoolClusterRegistry(changes.clusterConsumers());
        clients = new TarantoolClientRegistry(changes.clientConsumers());
    }

    public Supplier<TarantoolCluster> getCluster(String clusterId){
        return clusters.get(clusterId);
    }

    public Supplier<TarantoolClient> getClient(String clusterId, String clientId){
        return clients.get(clusterId, clientId);
    }
}
