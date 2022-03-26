package io.art.tarantool.module;

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.manager.*;
import io.art.tarantool.refresher.*;
import io.art.tarantool.state.*;
import lombok.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.tarantool.configuration.TarantoolModuleConfiguration.*;
import static reactor.core.publisher.Hooks.*;

@Getter
public class TarantoolModule implements StatefulModule<TarantoolModuleConfiguration, Configurator, TarantoolModuleState> {
    private static final LazyProperty<StatefulModuleProxy<TarantoolModuleConfiguration, TarantoolModuleState>> tarantoolModule = lazy(() -> context().getStatefulModule(TarantoolModule.class.getSimpleName()));
    private final String id = TarantoolModule.class.getSimpleName();
    private final TarantoolModuleRefresher refresher = new TarantoolModuleRefresher();
    private final TarantoolModuleConfiguration configuration = new TarantoolModuleConfiguration(refresher);
    private final TarantoolManager manager = new TarantoolManager(configuration);
    private final Configurator configurator = new Configurator(configuration);
    private final TarantoolModuleState state = new TarantoolModuleState();

    public static StatefulModuleProxy<TarantoolModuleConfiguration, TarantoolModuleState> tarantoolModule() {
        return tarantoolModule.get();
    }

    @Override
    public void launch(ContextService contextService) {
        onErrorDropped(emptyConsumer());
        if (!configuration.getStorageConfigurations().isEmpty()) {
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
