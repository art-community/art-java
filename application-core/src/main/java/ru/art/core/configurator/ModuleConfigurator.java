package ru.art.core.configurator;

import ru.art.core.module.Module;
import ru.art.core.module.ModuleConfiguration;
import ru.art.core.module.ModuleState;

@FunctionalInterface
public interface ModuleConfigurator<C extends ModuleConfiguration, S extends ModuleState> {
    ModuleConfiguration configure(Module<C, S> module);
}
