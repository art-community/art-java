package io.art.tarantool.cluster;

import io.art.core.network.balancer.*;
import io.art.tarantool.client.*;
import io.art.tarantool.configuration.*;
import lombok.*;
import java.util.*;


@NonNull
public class TarantoolConnector {
    private final String clusterId;
    private Map<String, TarantoolClient> readWriteClients;
    private Map<String, TarantoolClient> readOnlyClients;
    private final Balancer<TarantoolClient> readOnlyBalancer;
    private final Balancer<TarantoolClient> readWriteBalancer;


    public TarantoolConnector(String clusterId, TarantoolClusterConfiguration configuration) {
        this.clusterId = clusterId;
        readOnlyBalancer = new RoundRobinBalancer<>();
        readWriteBalancer = new RoundRobinBalancer<>();
        initializeClients(configuration);
        readWriteBalancer.endpoints(readWriteClients.values());
        readOnlyBalancer.endpoints(readOnlyClients.values());
    }

    private void initializeClients(TarantoolClusterConfiguration configuration) {
        Map<String, TarantoolClientConfiguration> instanceConfigs = configuration.instances;

    }

    public TarantoolClient selectReadableClient() {
        return readOnlyBalancer.select();
    }

    public TarantoolClient selectWritableClient() {
        return readWriteBalancer.select();
    }
}
