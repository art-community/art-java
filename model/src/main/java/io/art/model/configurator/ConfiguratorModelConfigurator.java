package io.art.model.configurator;

import io.art.model.implementation.configurator.*;
import static io.art.core.factory.SetFactory.*;
import static java.util.Arrays.*;
import java.util.*;

public class ConfiguratorModelConfigurator {
    private final Set<Class<?>> customConfigurations = set();

    public ConfiguratorModelConfigurator configuration(Class<?>... modelClasses) {
        customConfigurations.addAll(asList(modelClasses));
        return this;
    }

    public ConfiguratorModelConfigurator configurations(Class<?>... modelClasses) {
        customConfigurations.addAll(asList(modelClasses));
        return this;
    }

    public ConfiguratorModelConfigurator configurations(Collection<Class<?>> modelClasses) {
        customConfigurations.addAll(modelClasses);
        return this;
    }

    ConfiguratorModuleModel configure() {
        return new ConfiguratorModuleModel(immutableSetOf(customConfigurations));
    }
}
