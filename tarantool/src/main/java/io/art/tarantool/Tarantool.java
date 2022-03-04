package io.art.tarantool;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.storage.*;
import io.art.tarantool.service.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static lombok.AccessLevel.*;

@Public
@UtilityClass
public class Tarantool {
    private final static TarantoolProvider provider = new TarantoolProvider();

    public static <T extends Storage> T tarantool(Class<T> storageClass) {
        Communicator communicator = tarantoolModule().configuration()
                .getCommunicator()
                .getCommunicators()
                .getCommunicator(() -> idByDash(storageClass), storageClass)
                .getCommunicator();
        return cast(communicator);
    }

    public static TarantoolProvider tarantool() {
        return provider;
    }

    @Public
    @NoArgsConstructor(access = PRIVATE)
    public static class TarantoolProvider {
        public <KeyType, ValueType> SpaceService<KeyType, ValueType> space(Class<ValueType> type) {
            return tarantoolModule().configuration().getServices().getSpace(type);
        }

        public TarantoolSchemaService schema(Class<? extends Storage> type) {
            return tarantoolModule().configuration().getServices().getSchema(type);
        }
    }
}
