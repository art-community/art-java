package ru.adk.logging;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.logging.LoggingModuleConfiguration.LoggingModuleDefaultConfiguration;
import static java.util.logging.LogManager.getLogManager;
import static org.slf4j.bridge.SLF4JBridgeHandler.install;
import static ru.adk.core.context.Context.context;
import static ru.adk.logging.LoggingModuleConstants.LOGGING_MODULE_ID;

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
