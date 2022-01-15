package io.art.tarantool.configuration;

import io.art.core.source.*;
import io.art.tarantool.refresher.*;
import lombok.*;
import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.checker.NullityChecker.orElse;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.tarantool.configuration.TarantoolClientConfiguration.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import java.util.*;

@Getter
public class TarantoolConnectorConfiguration {
    private Map<String, TarantoolClientConfiguration> clients = map();
    private boolean logging;

    public static TarantoolConnectorConfiguration tarantoolConnectorConfiguration(ConfigurationSource source, TarantoolModuleRefresher refresher) {
        TarantoolConnectorConfiguration configuration = new TarantoolConnectorConfiguration();

        ConfigurationSource instancesConfiguration = source.getNested(TARANTOOL_INSTANCES_SECTION);

        configuration.clients = instancesConfiguration.getKeys()
                .stream()
                .collect(toImmutableMap(key -> key, key -> tarantoolClientConfiguration(instancesConfiguration.getNested(key))));

        configuration.clients.forEach((key, value) -> refresher.clientListeners().listenerFor(source.getParent() + COLON + key).emit(value));
        configuration.logging = orElse(source.getBoolean(TARANTOOL_LOGGING_KEY), false);

        refresher.clusterListeners().listenerFor(source.getParent()).emit(configuration);

        return configuration;
    }
}
