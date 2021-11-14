package io.art.tarantool.module.connection.registry;

import io.art.core.changes.*;
import io.art.core.property.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.module.connection.client.*;
import lombok.*;

import java.util.*;

import static io.art.core.factory.MapFactory.*;
import static io.art.core.property.Property.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.text.MessageFormat.*;
import static java.util.Optional.*;

@RequiredArgsConstructor
public class TarantoolClusterRegistry {
    private final ChangesConsumerRegistry changes;
    private final Map<String, Property<TarantoolCluster>> clusters = map();

    public Property<TarantoolCluster> get(String clusterId){
        return ofNullable(clusters.get(clusterId))
                .orElse(create(clusterId));
    }

    private Property<TarantoolCluster> create(String clusterId){
        Property<TarantoolCluster> newCluster = ofNullable(tarantoolModule().configuration().clusters.get(clusterId))
                .map(clusterConfiguration -> property(
                        () -> new TarantoolCluster(clusterId, clusterConfiguration))
                        .listenConsumer(() -> changes.consumerFor(clusterId)))
                .orElseThrow(() -> new TarantoolModuleException(format(CLUSTER_CONFIGURATION_IS_NULL, clusterId)));
        clusters.put(clusterId, newCluster);
        return newCluster;
    }
}
