package io.art.model.customizer;


import io.art.core.annotation.UsedByGenerator;
import io.art.storage.configuration.StorageModuleConfiguration;
import io.art.storage.registry.StorageSpacesRegistry;
import lombok.Getter;

@Getter
@UsedByGenerator
public class StorageCustomizer {
    @Getter
    private final Custom configuration = new Custom();

    public StorageCustomizer registry(StorageSpacesRegistry registry) {
        configuration.spacesRegistry = registry;
        return this;
    }

    @Getter
    private static class Custom extends StorageModuleConfiguration {
        private StorageSpacesRegistry spacesRegistry;
    }
}
