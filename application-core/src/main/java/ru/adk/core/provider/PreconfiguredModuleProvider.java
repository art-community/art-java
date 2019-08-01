package ru.adk.core.provider;

import ru.adk.core.module.ModuleConfiguration;
import java.util.Optional;

public interface PreconfiguredModuleProvider {
    <T extends ModuleConfiguration> Optional<T> getModuleConfiguration(String moduleId);
}
