package io.art.model.customizer;


import io.art.core.annotation.UsedByGenerator;
import io.art.storage.configuration.StorageModuleConfiguration;
import io.art.storage.registry.StorageSpacesRegistry;
import lombok.Getter;

@UsedByGenerator
public class StorageCustomizer {
    private final Custom configuration = new Custom();

    public StorageCustomizer registry(StorageSpacesRegistry registry) {
        configuration.registry = registry;
        return this;
    }

    @Getter
    private static class Custom extends StorageModuleConfiguration {
        private StorageSpacesRegistry registry;
    }
}
