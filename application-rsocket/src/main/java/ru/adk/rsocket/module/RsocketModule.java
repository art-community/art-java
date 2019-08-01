package ru.adk.rsocket.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.rsocket.configuration.RsocketModuleConfiguration;
import ru.adk.rsocket.state.RsocketModuleState;
import static ru.adk.core.context.Context.context;
import static ru.adk.rsocket.configuration.RsocketModuleConfiguration.RsocketModuleDefaultConfiguration;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RSOCKET_MODULE_ID;

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
