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
public class TarantoolStorageConfiguration {
    private String storage;
    private ImmutableSet<TarantoolClientConfiguration> clients;
    private boolean logging;

    public static TarantoolStorageConfiguration tarantoolStorageConfiguration(String storage) {
        TarantoolStorageConfiguration configuration = TarantoolStorageConfiguration.builder().build();
        configuration.storage = storage;
        configuration.clients = emptyImmutableSet();
        configuration.logging = false;
        return configuration;
    }

    public static TarantoolStorageConfiguration tarantoolStorageConfiguration(ConfigurationSource source, TarantoolModuleRefresher refresher) {
        TarantoolStorageConfiguration configuration = TarantoolStorageConfiguration.builder().build();


        configuration.clients = source.getNestedArray(TARANTOOL_INSTANCES_SECTION, TarantoolClientConfiguration::tarantoolClientConfiguration).asSet();
        configuration.logging = orElse(source.getBoolean(TARANTOOL_LOGGING_KEY), false);

        refresher.clusterListeners().listenerFor(source.getParent()).emit(configuration);

        return configuration;
    }
}
