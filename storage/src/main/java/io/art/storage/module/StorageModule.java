package io.art.storage.module;

import io.art.core.exception.NotImplementedException;
import io.art.core.module.StatefulModule;
import io.art.core.module.StatefulModuleProxy;
import io.art.storage.configuration.StorageModuleConfiguration;
import io.art.storage.module.state.StorageModuleState;
import io.art.storage.record.StorageRecord;
import io.art.storage.space.StorageSpace;
import io.art.value.immutable.Value;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

import static io.art.core.context.Context.context;
import static io.art.storage.configuration.StorageModuleConfiguration.Configurator;

@Getter
public class StorageModule implements StatefulModule<StorageModuleConfiguration, Configurator, StorageModuleState> {
    private static final StatefulModuleProxy<StorageModuleConfiguration, StorageModuleState> storageModule =
            context().getStatefulModule(StorageModule.class.getSimpleName());

    private final String id = StorageModule.class.getSimpleName();
    private final StorageModuleConfiguration configuration = new StorageModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    private final StorageModuleState state = new StorageModuleState();

    public List<String> spaces(){
        throw new NotImplementedException("TODO");
    }

    public StorageSpace<Value> space(String spaceID){
        throw new NotImplementedException("TODO");
    }

    public <T> StorageSpace<T> space(String spaceID, Function<Value, T> mapper){
        throw new NotImplementedException("TODO");
    }
}
