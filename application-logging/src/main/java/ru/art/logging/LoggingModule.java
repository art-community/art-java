package ru.art.logging;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.logging.LoggingModuleConfiguration.LoggingModuleDefaultConfiguration;
import static java.util.logging.LogManager.getLogManager;
import static org.slf4j.bridge.SLF4JBridgeHandler.install;
import static ru.art.core.context.Context.context;
import static ru.art.logging.LoggingModuleConstants.LOGGING_MODULE_ID;

@Getter
public class LoggingModule implements Module<LoggingModuleConfiguration, ModuleState> {
    static {
        getLogManager().reset();
        install();
    }

    @Getter
    private final LoggingModuleConfiguration defaultConfiguration = new LoggingModuleDefaultConfiguration();
    private final String id = LOGGING_MODULE_ID;

    public static LoggingModuleConfiguration loggingModule() {
        return context().getModule(LOGGING_MODULE_ID, new LoggingModule());
    }
}
