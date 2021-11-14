package io.art.configurator;

import io.art.configurator.exception.*;
import io.art.configurator.model.*;
import io.art.configurator.source.*;
import io.art.core.annotation.*;
import io.art.core.source.*;
import lombok.experimental.*;
import static io.art.configurator.module.ConfiguratorModule.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;

@Public
@UtilityClass
public class Configuring {
    public static ConfigurationSource configuration() {
        return configuratorModule().configuration().getConfiguration();
    }

    public static ConfigurationSource configuration(String section) {
        return orThrow(configuration().getNested(section), () -> new ConfigurationNotFoundException(section));
    }

    public static <T> T configuration(Class<T> type) {
        return configuration(EMPTY_STRING, type);
    }

    public static <T> T configuration(String section, Class<T> type) {
        T customConfiguration = configuratorModule()
                .configuration()
                .getCustomConfiguration(new CustomConfiguration(section, type));
        return orThrow(customConfiguration, () -> new ConfigurationNotFoundException(section));
    }

    public static PropertiesConfigurationSource properties() {
        return configuratorModule().configuration().getProperties();
    }

    public static EnvironmentConfigurationSource environment() {
        return configuratorModule().configuration().getEnvironment();
    }
}
