package io.art.model.customizer;


import io.art.core.annotation.*;
import io.art.core.module.*;
import io.art.storage.configuration.*;
import io.art.storage.module.*;
import io.art.storage.registry.*;
import lombok.*;

@UsedByGenerator
public class StorageInitializer implements ModuleInitializer<StorageModuleConfiguration, StorageModuleConfiguration.Configurator, StorageModule> {
    private final Initial configuration = new Initial();

    public StorageInitializer registry(StorageSpacesRegistry registry) {
        configuration.spacesRegistry = registry;
        return this;
    }

    @Override
    public StorageModuleConfiguration initialize(StorageModule module) {
        return configuration;
    }

    @Getter
    private static class Initial extends StorageModuleConfiguration {
        private StorageSpacesRegistry spacesRegistry;
    }
}
