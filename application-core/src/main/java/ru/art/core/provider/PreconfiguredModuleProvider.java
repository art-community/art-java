package ru.art.core.provider;

import ru.art.core.module.ModuleConfiguration;
import java.util.Optional;

public interface PreconfiguredModuleProvider {
    <T extends ModuleConfiguration> Optional<T> getModuleConfiguration(String moduleId);
}
