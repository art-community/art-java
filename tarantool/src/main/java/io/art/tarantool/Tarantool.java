package io.art.tarantool;

import io.art.core.annotation.*;
import io.art.storage.*;
import lombok.experimental.*;
import static io.art.tarantool.module.TarantoolModule.*;

@Public
@UtilityClass
public class Tarantool {
    public static <T extends Storage> T tarantoolStorage(Class<T> storageClass) {
        return tarantoolModule().configuration().getCommunicator().getConnectors().getConnector(storageClass);
    }
}
