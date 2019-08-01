package ru.art.tarantool.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.tarantool.configuration.TarantoolModuleConfiguration;
import ru.art.tarantool.configuration.TarantoolModuleConfiguration.TarantoolModuleDefaultConfiguration;
import ru.art.tarantool.initializer.TarantoolInitializer;
import ru.art.tarantool.state.TarantoolModuleState;
import static java.util.Map.Entry;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TARANTOOL_MODULE_ID;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInitializationMode.ON_MODULE_LOAD;

@Getter
public class TarantoolModule implements Module<TarantoolModuleConfiguration, TarantoolModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static TarantoolModuleConfiguration tarantoolModule = context().getModule(TARANTOOL_MODULE_ID, new TarantoolModule());
    @Getter(lazy = true, value = PRIVATE)
    private final static TarantoolModuleState tarantoolModuleState = context().getModuleState(TARANTOOL_MODULE_ID, new TarantoolModule());
    private final String id = TARANTOOL_MODULE_ID;
    private final TarantoolModuleConfiguration defaultConfiguration = new TarantoolModuleDefaultConfiguration();
    @Getter(lazy = true)
    private final TarantoolModuleState state = new TarantoolModuleState();

    @Override
    public void onLoad() {
        if (tarantoolModule().getInitializationMode() != ON_MODULE_LOAD) {
            return;
        }
        tarantoolModule().getTarantoolConfigurations()
                .entrySet()
                .stream()
                .filter(entry -> nonNull(entry) && nonNull(entry.getKey()) && nonNull(entry.getValue()))
                .map(Entry::getKey)
                .forEach(TarantoolInitializer::initializeTarantool);
    }

    public static TarantoolModuleConfiguration tarantoolModule() {
        return getTarantoolModule();
    }

    public static TarantoolModuleState tarantoolModuleState() {
        return getTarantoolModuleState();
    }
}
