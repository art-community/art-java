package io.art.tarantool;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.storage.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
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
        public <KeyType, ModelType> BlockingSpaceService<KeyType, ModelType> space(Class<ModelType> spaceType) {
            return tarantoolModule().configuration().getServices().getSpace(spaceType);
        }

        public TarantoolSchemaService schema(Class<? extends Storage> type) {
            return tarantoolModule().configuration().getServices().getSchema(type);
        }

        public <ModelType, IndexesType extends Indexes<ModelType>> IndexesType indexes(Class<ModelType> spaceType) {
            return tarantoolModule().configuration().getServices().getIndexes(spaceType);
        }

        public <ModelType, IndexesType extends Sharders<ModelType>> IndexesType sharders(Class<ModelType> spaceType) {
            return tarantoolModule().configuration().getServices().getSharders(spaceType);
        }
    }
}
