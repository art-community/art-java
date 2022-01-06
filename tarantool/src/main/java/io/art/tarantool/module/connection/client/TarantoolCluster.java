package io.art.tarantool.module.connection.client;

import io.art.core.network.balancer.*;
import io.art.tarantool.configuration.*;
import io.tarantool.driver.api.*;
import lombok.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import static com.google.common.collect.ImmutableMap.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static io.art.tarantool.module.connection.client.caller.TarantoolFunctionCaller.*;


@NonNull
public class TarantoolCluster {
    private final String clusterId;
    private Map<String, Supplier<TarantoolClient>> readWriteClients;
    private Map<String, Supplier<TarantoolClient>> readOnlyClients;
    private final Balancer<Supplier<TarantoolClient>> readOnlyBalancer;
    private final Balancer<Supplier<TarantoolClient>> readWriteBalancer;


    public TarantoolCluster(String clusterId, TarantoolClusterConfiguration configuration){
        this.clusterId = clusterId;
        readOnlyBalancer = new RoundRobinBalancer<>();
        readWriteBalancer = new RoundRobinBalancer<>();
        getAllClients(configuration);
        readWriteBalancer.updateEndpoints(readWriteClients.values());
        readOnlyBalancer.updateEndpoints(readOnlyClients.values());
    }

    public CompletableFuture<List<?>> callRW(String function, Object... args){
        return call(readWriteBalancer.select().get(), function, args);
    }

    public CompletableFuture<List<?>> callRO(String function, Object... args){
        return call(readOnlyBalancer.select().get(), function, args);
    }

    private void getAllClients(TarantoolClusterConfiguration configuration){
        Map<String, TarantoolConnectorConfiguration> instanceConfigs = configuration.instances;
        readOnlyClients =
                instanceConfigs.keySet().stream()
                        .filter(id -> instanceConfigs.get(id).isReadable())
                        .collect(toImmutableMap(id -> id, this::getClient));

        readWriteClients =
                instanceConfigs.keySet().stream()
                        .filter(id -> instanceConfigs.get(id).isWriteable())
                        .collect(toImmutableMap(id -> id, this::getClient));

    }

    public Supplier<TarantoolClient> getClient(String clientId){
        return tarantoolModule().state().getClient(clusterId, clientId);
    }

}
