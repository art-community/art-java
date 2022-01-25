package io.art.tarantool.factory;

import io.art.tarantool.configuration.*;
import io.art.tarantool.space.*;
import io.art.tarantool.storage.*;
import lombok.experimental.*;
import static io.art.tarantool.module.TarantoolModule.*;

@UtilityClass
public class TarantoolCommunicationFactory {
    public static TarantoolSpaceCommunication createConfiguredTarantoolCommunication(TarantoolStorageConfiguration storageConfiguration) {
        String storage = storageConfiguration.getStorage();
        TarantoolModuleConfiguration moduleConfiguration = tarantoolModule().configuration();
        return new TarantoolSpaceCommunication(() -> new TarantoolStorage(moduleConfiguration.getStorages().get(storage)), moduleConfiguration);
    }

    public static TarantoolSpaceCommunication createDefaultTarantoolCommunication(TarantoolStorageConfiguration storageConfiguration) {
        return new TarantoolSpaceCommunication(() -> new TarantoolStorage(storageConfiguration), tarantoolModule().configuration());
    }
}
