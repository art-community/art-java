package io.art.tarantool.registry;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.storage.*;
import io.art.tarantool.service.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;

@RequiredArgsConstructor
public class TarantoolServiceRegistry {
    private final LazyProperty<ImmutableMap<String, TarantoolSpaceService<?, ?>>> spaces;
    private final LazyProperty<ImmutableMap<String, TarantoolSchemaService>> schemas;

    public <KeyType, ValueType extends Space> TarantoolSpaceService<KeyType, ValueType> getSpace(Class<ValueType> type) {
        return cast(spaces.get().get(idByDot(type)));
    }

    public TarantoolSchemaService getSchema(Class<? extends Storage> storageType) {
        return cast(schemas.get().get(idByDash(storageType)));
    }
}
