package io.art.storage.mapper;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import lombok.*;
import static lombok.AccessLevel.*;

@Getter
@RequiredArgsConstructor(access = PACKAGE)
public class MapperBySpace<Current, Other> {
    private final MetaClass<Other> mappingSpace;
    private MetaField<? extends MetaClass<Current>, ?> mappingKeyField;

    private Index mappingIndex;
    private Tuple mappingIndexTuple;

    MapperBySpace<Current, Other> bySpace(MetaField<? extends MetaClass<Current>, ?> mappingField) {
        mappingKeyField = mappingField;
        return this;
    }

    final MapperBySpace<Current, Other> byIndex(Index index, Tuple tuple) {
        mappingIndex = index;
        mappingIndexTuple = tuple;
        return this;
    }
}
