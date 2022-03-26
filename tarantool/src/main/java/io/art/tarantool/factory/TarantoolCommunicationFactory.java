package io.art.tarantool.factory;

import io.art.tarantool.communicator.*;
import io.art.tarantool.configuration.*;
import lombok.experimental.*;
import static io.art.tarantool.module.TarantoolModule.*;

@UtilityClass
public class TarantoolCommunicationFactory {
    public static TarantoolCommunication createTarantoolCommunication(TarantoolStorageConfiguration storageConfiguration) {
        String storage = storageConfiguration.getStorage();
        TarantoolModuleConfiguration moduleConfiguration = tarantoolModule().configuration();
        return new TarantoolCommunication(moduleConfiguration.storageRegistry(storage), moduleConfiguration);
    }
}
