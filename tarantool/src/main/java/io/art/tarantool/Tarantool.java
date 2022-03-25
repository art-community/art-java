package io.art.tarantool;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.storage.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.service.schema.*;
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
        public <KeyType, SpaceType> BlockingSpaceService<KeyType, SpaceType> space(Class<? extends Storage> storageType, Class<SpaceType> spaceType) {
            return tarantoolModule().configuration().storageRegistry(() -> idByDash(storageType)).getSpace(spaceType);
        }

        public TarantoolSchemaService schema(Class<? extends Storage> type) {
            return tarantoolModule().configuration().storageRegistry(() -> idByDash(type)).getSchema();
        }

        public TarantoolStorageConnector connector(Class<? extends Storage> type) {
            return tarantoolModule().configuration().storageRegistry(() -> idByDash(type)).getConnector();
        }

        public <SpaceType, IndexesType extends Indexes<SpaceType>> IndexesType indexes(Class<? extends Storage> storageType, Class<SpaceType> spaceType) {
            return tarantoolModule().configuration().storageRegistry(() -> idByDash(storageType)).getIndexes(spaceType);
        }

        public <SpaceType, IndexesType extends Sharders<SpaceType>> IndexesType sharders(Class<? extends Storage> storageType, Class<SpaceType> spaceType) {
            return tarantoolModule().configuration().storageRegistry(() -> idByDash(storageType)).getSharders(spaceType);
        }
    }
}
