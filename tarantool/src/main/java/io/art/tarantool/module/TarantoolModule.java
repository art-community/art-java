package io.art.tarantool.module;

import io.art.core.module.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.instance.*;
import io.art.tarantool.module.client.*;
import io.art.tarantool.module.state.*;
import lombok.*;

import java.util.*;

import static io.art.core.context.Context.*;
import static io.art.tarantool.configuration.TarantoolModuleConfiguration.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

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

    public static TarantoolClusterClient getCluster(String clusterId) {
        Optional<TarantoolClusterClient> existingClient = tarantoolModule().state().getClusterClient(clusterId);
        if (existingClient.isPresent()) return existingClient.get();
        TarantoolClusterConfiguration config = tarantoolModule().configuration().clusters.get(clusterId);
        if (isNull(config)) throw new TarantoolModuleException(format(CLUSTER_CONFIGURATION_IS_NULL, clusterId));
        TarantoolClusterClient newClient = new TarantoolClusterClient(clusterId, config);
        tarantoolModule().state().registerClusterClient(clusterId, newClient);
        return newClient;
    }
}
