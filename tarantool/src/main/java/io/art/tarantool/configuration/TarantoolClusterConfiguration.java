package io.art.tarantool.configuration;

import io.art.core.network.balancer.Balancer.*;
import io.art.core.source.*;
import io.art.tarantool.module.refresher.*;
import lombok.*;

import java.util.*;

import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;

@EqualsAndHashCode
public class TarantoolClusterConfiguration {
    public Map<String, TarantoolInstanceConfiguration> instances = map();
    public BalancerMethod balancerMethod = BalancerMethod.ROUND_ROBIN;

    public static TarantoolClusterConfiguration from(ConfigurationSource source, TarantoolModuleRefresher refresher) {
        TarantoolClusterConfiguration configuration = new TarantoolClusterConfiguration();

        ConfigurationSource instancesConfiguration = source.getNested(TARANTOOL_INSTANCES_SECTION);
        configuration.instances = instancesConfiguration.getKeys()
                        .stream()
                        .collect(toImmutableMap(key -> key, key -> TarantoolInstanceConfiguration.from(instancesConfiguration.getNested(key))));

        configuration.instances.forEach((key, value) ->
                refresher.clientListeners().listenerFor(source.getParent() + COLON + key).emit(value));

        refresher.clusterListeners().listenerFor(source.getParent()).emit(configuration);
        return configuration;
    }
}
