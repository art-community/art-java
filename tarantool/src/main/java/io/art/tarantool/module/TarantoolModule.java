package io.art.tarantool.module;

import io.art.core.collection.*;
import io.art.core.context.*;
import io.art.core.module.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.manager.*;
import io.art.tarantool.refresher.*;
import io.art.tarantool.state.*;
import lombok.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.context.Context.*;
import static io.art.tarantool.configuration.TarantoolModuleConfiguration.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Hooks.*;

@Getter
public class TarantoolModule implements StatefulModule<TarantoolModuleConfiguration, Configurator, TarantoolModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<TarantoolModuleConfiguration, TarantoolModuleState> tarantoolModule = context().getStatefulModule(TarantoolModule.class.getSimpleName());
    private final String id = TarantoolModule.class.getSimpleName();
    private final TarantoolModuleRefresher refresher = new TarantoolModuleRefresher();
    private final TarantoolModuleConfiguration configuration = new TarantoolModuleConfiguration(refresher);
    private final TarantoolManager manager = new TarantoolManager(configuration);
    private final Configurator configurator = new Configurator(configuration);
    private final TarantoolModuleState state = new TarantoolModuleState();

    public static StatefulModuleProxy<TarantoolModuleConfiguration, TarantoolModuleState> tarantoolModule() {
        return getTarantoolModule();
    }

    @Override
    public void launch(ContextService contextService) {
        onErrorDropped(emptyConsumer());
        ImmutableMap<String, TarantoolStorageConfiguration> storageConfigurations = configuration.getStorageConfigurations();
        if (!storageConfigurations.isEmpty()) {
            manager.initialize();
        }
    }

    @Override
    public void shutdown(ContextService contextService) {
        if (!configuration.getStorageConfigurations().isEmpty()) {
            manager.dispose();
        }
    }
}
