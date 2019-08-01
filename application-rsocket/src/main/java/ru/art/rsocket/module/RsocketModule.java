package ru.art.rsocket.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.rsocket.configuration.RsocketModuleConfiguration;
import ru.art.rsocket.state.RsocketModuleState;
import static ru.art.core.context.Context.context;
import static ru.art.rsocket.configuration.RsocketModuleConfiguration.RsocketModuleDefaultConfiguration;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_MODULE_ID;

@Getter
public class RsocketModule implements Module<RsocketModuleConfiguration, RsocketModuleState> {
    @Getter(lazy = true)
    private static final RsocketModuleConfiguration rsocketModule = context().getModule(RSOCKET_MODULE_ID, new RsocketModule());
    @Getter(lazy = true)
    private static final RsocketModuleState rsocketModuleState = context().getModuleState(RSOCKET_MODULE_ID, new RsocketModule());
    private final String id = RSOCKET_MODULE_ID;
    private final RsocketModuleConfiguration defaultConfiguration = new RsocketModuleDefaultConfiguration();
    private final RsocketModuleState state = new RsocketModuleState();

    public static RsocketModuleConfiguration rsocketModule() {
        return getRsocketModule();
    }

    public static RsocketModuleState rsocketModuleState() {
        return getRsocketModuleState();
    }
}
