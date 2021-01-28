package io.art.storage.module;

import io.art.core.exception.NotImplementedException;
import io.art.core.module.StatefulModule;
import io.art.core.module.StatefulModuleProxy;
import io.art.storage.configuration.StorageModuleConfiguration;
import io.art.storage.module.state.StorageModuleState;
import io.art.storage.space.Space;
import io.art.value.immutable.Value;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.art.core.caster.Caster.cast;
import static io.art.core.context.Context.context;
import static io.art.storage.configuration.StorageModuleConfiguration.Configurator;
import static lombok.AccessLevel.PRIVATE;

@Getter
public class StorageModule implements StatefulModule<StorageModuleConfiguration, Configurator, StorageModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<StorageModuleConfiguration, StorageModuleState> storageModule =
            context().getStatefulModule(StorageModule.class.getSimpleName());

    private final String id = StorageModule.class.getSimpleName();
    private final StorageModuleConfiguration configuration = new StorageModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    private final StorageModuleState state = new StorageModuleState();

    public static StatefulModuleProxy<StorageModuleConfiguration, StorageModuleState> storageModule() {
        return getStorageModule();
    }

    public static <T extends Space<?,?>> T space(Class<?> spaceModelClass){
        return space(spaceModelClass.getSimpleName());
    }

    public static <T extends Space<?,?>> T space(String spaceID){
        return cast(storageModule().configuration().getSpacesRegistry().get(spaceID));
    }
}
