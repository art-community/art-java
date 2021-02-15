package io.art.tarantool.module.connection.registry;

import io.art.core.changes.*;
import io.art.core.property.*;
import io.art.tarantool.exception.*;
import io.tarantool.driver.api.*;
import lombok.*;

import java.util.*;

import static io.art.core.constants.StringConstants.COLON;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.property.Property.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static io.art.tarantool.module.connection.client.connector.TarantoolConnector.*;
import static java.text.MessageFormat.*;
import static java.util.Optional.*;

@RequiredArgsConstructor
public class TarantoolClientRegistry {
    private final ChangesConsumerRegistry changes;
    private final Map<String, Property<TarantoolClient>> clients = map();

    public Property<TarantoolClient> get(String clusterId, String clientId){
        return ofNullable(clients.get(clusterId + COLON + clientId))
                .orElseGet(() -> create(clusterId, clientId));
    }

    private Property<TarantoolClient> create(String clusterId, String clientId){
                return ofNullable(tarantoolModule().configuration().clusters.get(clusterId))
                        .map(clusterConfig -> clusterConfig.instances.get(clientId))
                        .map(clientConfig -> register(clusterId, clientId, property(() ->
                                connect(clientId, clientConfig))
                                .listenConsumer(() -> changes.consumerFor(clusterId + COLON + clientId))
                        ))
                        .orElseThrow(() -> new TarantoolModuleException(format(CONFIGURATION_IS_NULL, clusterId, clientId)));
    }

    private Property<TarantoolClient> register(String clusterId, String clientId, Property<TarantoolClient> newClient){
        clients.put(clusterId + COLON + clientId, newClient);
        return newClient;
    }
}
