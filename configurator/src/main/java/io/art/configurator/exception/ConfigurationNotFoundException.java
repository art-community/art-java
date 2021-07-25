package io.art.configurator.exception;

import static io.art.configurator.constants.ConfiguratorModuleConstants.Errors.*;
import static java.text.MessageFormat.*;

public class ConfigurationNotFoundException extends RuntimeException {
    public ConfigurationNotFoundException(String section) {
        super(format(CONFIGURATION_WAS_NOT_FOUND, section));
    }
}
