package ru.adk.core.configurator;

import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.core.module.ModuleState;

@FunctionalInterface
public interface ModuleConfigurator<C extends ModuleConfiguration, S extends ModuleState> {
    ModuleConfiguration configure(Module<C, S> module);
}
