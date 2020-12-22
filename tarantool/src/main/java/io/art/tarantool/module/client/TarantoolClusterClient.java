package io.art.tarantool.module.client;

import io.art.core.network.balancer.Balancer;
import io.art.core.network.balancer.RoundRobinBalancer;
import io.art.tarantool.configuration.TarantoolClusterConfiguration;
import io.art.tarantool.configuration.TarantoolInstanceConfiguration;
import io.art.tarantool.exception.TarantoolModuleException;
import lombok.NonNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import io.tarantool.driver.api.TarantoolClient;


import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static io.art.core.caster.Caster.cast;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static io.art.tarantool.module.client.caller.TarantoolFunctionCaller.asynchronousCall;
import static io.art.tarantool.module.client.caller.TarantoolFunctionCaller.call;
import static io.art.tarantool.module.client.connector.TarantoolConnector.connect;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static io.art.tarantool.module.TarantoolModule.tarantoolModule;


@NonNull
public class TarantoolClusterClient {
    private final String clusterId;
    private Map<String, TarantoolClient> readWriteClients;
    private Map<String, TarantoolClient> readOnlyClients;
    private final Balancer<TarantoolClient> readOnlyBalancer;
    private final Balancer<TarantoolClient> readWriteBalancer;


    public TarantoolClusterClient(String clusterId, TarantoolClusterConfiguration configuration){
        this.clusterId = clusterId;
        readOnlyBalancer = new RoundRobinBalancer<>();
        readWriteBalancer = new RoundRobinBalancer<>();
        getAllClients(configuration);
        readWriteBalancer.updateEndpoints(readWriteClients.values());
        readOnlyBalancer.updateEndpoints(readOnlyClients.values());
    }

    public CompletableFuture<List<?>> callRW(String function, Object... args){
        TarantoolClient client = readWriteBalancer.select();
        return asynchronousCall(client, function, args);
    }

    public CompletableFuture<List<?>> callRO(String function, Object... args){
        return asynchronousCall(readOnlyBalancer.select(), function, args);
    }

    private void getAllClients(TarantoolClusterConfiguration configuration){
        Map<String, TarantoolInstanceConfiguration> instanceConfigs = configuration.instances;
        readOnlyClients = cast(
                instanceConfigs.keySet().stream()
                        .filter(id -> instanceConfigs.get(id).isReadable())
                        .collect(toImmutableMap(id -> id, this::getClient)));


        readWriteClients = cast(
                instanceConfigs.keySet().stream()
                        .filter(id -> instanceConfigs.get(id).isWriteable())
                        .collect(toImmutableMap(id -> id, this::getClient)));

    }

    public TarantoolClient getClient(String clientId){
        Optional<TarantoolClient> existingClient = tarantoolModule().state().getClient(clientId);
        if (existingClient.isPresent()) return existingClient.get();

        TarantoolInstanceConfiguration configuration = tarantoolModule().configuration().clusters.get(clusterId).instances.get(clientId);
        if (isNull(configuration)) throw new TarantoolModuleException(format(CONFIGURATION_IS_NULL, clientId));
        TarantoolClient newClient = connect(clientId, configuration);
        tarantoolModule().state().registerClient(clientId, newClient);
        return newClient;
    }

}
