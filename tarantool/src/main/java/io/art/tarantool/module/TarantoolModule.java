package io.art.tarantool.module;

import io.art.core.module.StatefulModule;
import io.art.core.module.StatefulModuleProxy;
import io.art.tarantool.configuration.TarantoolClusterConfiguration;
import io.art.tarantool.configuration.TarantoolModuleConfiguration;
import io.art.tarantool.exception.TarantoolModuleException;
import io.art.tarantool.module.client.TarantoolClusterClient;
import io.art.tarantool.module.state.TarantoolModuleState;
import io.art.tarantool.instance.TarantoolInstance;
import lombok.Getter;

import static io.art.core.context.Context.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_CLUSTER_NAME;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.CLUSTER_CONFIGURATION_IS_NULL;
import static java.text.MessageFormat.format;
import static io.art.tarantool.configuration.TarantoolModuleConfiguration.Configurator;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;

import java.util.Optional;

@Getter
public class TarantoolModule implements StatefulModule<TarantoolModuleConfiguration, Configurator, TarantoolModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<TarantoolModuleConfiguration, TarantoolModuleState> tarantoolModule =
            context().getStatefulModule(TarantoolModule.class.getSimpleName());
    private final TarantoolModuleConfiguration configuration = new TarantoolModuleConfiguration();
    private final TarantoolModuleState state = new TarantoolModuleState();
    private final String id = TarantoolModule.class.getSimpleName();
    private final Configurator configurator = new Configurator(configuration);

    public static StatefulModuleProxy<TarantoolModuleConfiguration, TarantoolModuleState> tarantoolModule() {
        return getTarantoolModule();
    }

    public static TarantoolInstance tarantoolInstance(){
        return tarantoolInstance(DEFAULT_CLUSTER_NAME);
    }

    public static TarantoolInstance tarantoolInstance(String clusterId){
        return new TarantoolInstance(getCluster(clusterId));
    }

    private static TarantoolClusterClient getCluster(String clusterId){
        Optional<TarantoolClusterClient> existingClient = tarantoolModule().state().getClusterClient(clusterId);
        if (existingClient.isPresent()) return existingClient.get();
        TarantoolClusterConfiguration config = tarantoolModule().configuration().clusters.get(clusterId);
        if (isNull(config)) throw new TarantoolModuleException(format(CLUSTER_CONFIGURATION_IS_NULL, clusterId));
        TarantoolClusterClient newClient = new TarantoolClusterClient(clusterId, config);
        tarantoolModule().state().registerClusterClient(clusterId, newClient);
        return newClient;
    }

    @Override
    public void onUnload() {

    }
}
