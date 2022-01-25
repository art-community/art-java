package io.art.tarantool.factory;

import io.art.tarantool.communication.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.storage.*;
import lombok.experimental.*;
import static io.art.tarantool.module.TarantoolModule.*;

@UtilityClass
public class TarantoolCommunicationFactory {
    public static TarantoolCommunication createConfiguredTarantoolCommunication(TarantoolStorageConfiguration storageConfiguration) {
        String storage = storageConfiguration.getStorage();
        TarantoolModuleConfiguration moduleConfiguration = tarantoolModule().configuration();
        return new TarantoolCommunication(() -> new TarantoolStorage(moduleConfiguration.getStorages().get(storage)), moduleConfiguration);
    }

    public static TarantoolCommunication createDefaultTarantoolCommunication(TarantoolStorageConfiguration storageConfiguration) {
        return new TarantoolCommunication(() -> new TarantoolStorage(storageConfiguration), tarantoolModule().configuration());
    }
}
