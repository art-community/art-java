package io.art.tarantool;

import io.art.core.annotation.*;
import io.art.storage.*;
import io.art.tarantool.service.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static lombok.AccessLevel.*;

@Public
@UtilityClass
public class Tarantool {
    private final static TarantoolProvider provider = new TarantoolProvider();

    public static <T extends Storage> T tarantool(Class<T> storageClass) {
        return tarantoolModule().configuration().getCommunicator().getPortals().getPortal(storageClass);
    }

    public static TarantoolProvider tarantool() {
        return provider;
    }

    @Public
    @NoArgsConstructor(access = PRIVATE)
    public static class TarantoolProvider {
        public <KeyType, ValueType extends Space> TarantoolSpaceService<KeyType, ValueType> space(Class<ValueType> type) {
            return tarantoolModule().configuration().getServices().getSpace(type);
        }

        public TarantoolSchemaService schema(Class<? extends Storage> type) {
            return tarantoolModule().configuration().getServices().getSchema(type);
        }
    }
}
