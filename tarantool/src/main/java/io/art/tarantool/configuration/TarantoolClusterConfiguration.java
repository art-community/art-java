package io.art.tarantool.configuration;

import io.art.core.source.ConfigurationSource;
import java.util.Map;
import io.art.core.network.balancer.Balancer.BalancerMethod;
import lombok.Getter;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static io.art.core.factory.MapFactory.map;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.TARANTOOL_INSTANCES_SECTION;


public class TarantoolClusterConfiguration {
    public Map<String, TarantoolInstanceConfiguration> instances = map();
    public BalancerMethod balancerMethod = BalancerMethod.ROUND_ROBIN;

    public static TarantoolClusterConfiguration from(ConfigurationSource source) {
        TarantoolClusterConfiguration configuration = new TarantoolClusterConfiguration();

        ConfigurationSource instancesConfiguration = source.getNested(TARANTOOL_INSTANCES_SECTION);
        configuration.instances = instancesConfiguration.getKeys()
                        .stream()
                        .collect(toImmutableMap(key -> key, key -> TarantoolInstanceConfiguration.from(instancesConfiguration.getNested(key))));
        return configuration;
    }
}
