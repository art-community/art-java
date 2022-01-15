package io.art.tarantool.configuration;

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.tarantool.refresher.*;
import lombok.Builder;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class TarantoolConnectorConfiguration {
    private String connector;
    private ImmutableSet<TarantoolClientConfiguration> clients;
    private boolean logging;

    public static TarantoolConnectorConfiguration tarantoolConnectorConfiguration(String connector) {
        TarantoolConnectorConfiguration configuration = TarantoolConnectorConfiguration.builder().build();
        configuration.connector = connector;
        configuration.clients = emptyImmutableSet();
        configuration.logging = false;
        return configuration;
    }

    public static TarantoolConnectorConfiguration tarantoolConnectorConfiguration(ConfigurationSource source, TarantoolModuleRefresher refresher) {
        TarantoolConnectorConfiguration configuration = TarantoolConnectorConfiguration.builder().build();


        configuration.clients = source.getNestedArray(TARANTOOL_INSTANCES_SECTION, TarantoolClientConfiguration::tarantoolClientConfiguration).asSet();
        configuration.logging = orElse(source.getBoolean(TARANTOOL_LOGGING_KEY), false);

        refresher.clusterListeners().listenerFor(source.getParent()).emit(configuration);

        return configuration;
    }
}
