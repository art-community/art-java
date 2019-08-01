package ru.adk.core.module;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModuleContainer<C extends ModuleConfiguration, S extends ModuleState> {
    private final Module<C, S> module;
    private C configuration;

    public ModuleContainer<C, S> overrideConfiguration(C newConfiguration) {
        configuration = newConfiguration;
        return this;
    }

    public void refreshConfiguration() {
        configuration.refresh();
    }

    public void reloadModule() {
        module.reload();
    }
}
