package io.art.logging;

import io.art.core.annotation.*;
import io.art.logging.configuration.*;
import io.art.logging.logger.*;
import io.art.logging.state.*;
import lombok.experimental.*;
import static io.art.logging.constants.LoggingModuleConstants.*;
import static io.art.logging.module.LoggingModule.*;
import java.util.*;

@ForUsing
@UtilityClass
public class Logging {
    public static Logger logger() {
        return logger(COMMON_LOGGER);
    }

    public static Logger logger(Class<?> type) {
        return logger(type.getName());
    }

    public static Logger logger(String name) {
        LoggingModuleConfiguration configuration = loggingModule().configuration();
        LoggingModuleState state = loggingModule().state();
        LoggerConfiguration loggerConfiguration = configuration
                .getLoggers()
                .entrySet()
                .stream()
                .filter(entry -> name.startsWith(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(configuration.getDefaultLogger());
        return state.register(name, loggerConfiguration);
    }
}
