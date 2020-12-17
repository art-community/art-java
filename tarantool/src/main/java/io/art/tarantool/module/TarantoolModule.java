package io.art.tarantool.module;

import io.art.core.module.StatefulModule;
import io.art.core.module.StatefulModuleProxy;
import io.art.core.module.Module;
import io.art.tarantool.configuration.TarantoolInstanceConfiguration;
import io.art.tarantool.configuration.TarantoolModuleConfiguration;
import io.art.tarantool.exception.TarantoolModuleException;
import io.art.tarantool.module.state.TarantoolModuleState;
import io.art.tarantool.dao.TarantoolInstance;
import io.tarantool.driver.api.TarantoolClient;
import lombok.Getter;

import static io.art.core.context.Context.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static io.art.tarantool.module.connector.TarantoolConnector.connect;
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

    public static TarantoolInstance tarantoolInstance(String clientId){
        return new TarantoolInstance(getClient(clientId));
    }

    public static TarantoolClient getClient(String clientId){
        Optional<TarantoolClient> existingClient = tarantoolModule().state().getClient(clientId);
        if (existingClient.isPresent()) return existingClient.get();
        TarantoolInstanceConfiguration config = tarantoolModule().configuration().instances.get(clientId);
        if (isNull(config)) throw new TarantoolModuleException(format(CONFIGURATION_IS_NULL, clientId));
        TarantoolClient newClient = connect(clientId, config);
        tarantoolModule().state().registerClient(clientId, newClient);
        return newClient;
    }

    @Override
    public void onUnload() {

    }
}
