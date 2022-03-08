package io.art.storage;

import io.art.meta.model.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import static io.art.storage.StorageConstants.MappingMode.*;

@Getter
public class Mapper<Current, Mapped> {
    private MappingMode mode;
    private MapperByField<?> byField;
    private MapperBySpace<?, ?> bySpace;
    private MapperBySpace<?, ?> byIndex;
    private MapperByFunction<?> byFunction;

    Mapper<Current, Mapped> byField(MetaField<? extends MetaClass<Current>, Mapped> field) {
        mode = FIELD;
        byField = new MapperByField<>(field);
        return this;
    }

    Mapper<Current, Mapped> bySpace(MetaClass<Mapped> otherSpace, MetaField<? extends MetaClass<Current>, ?> mappingField) {
        mode = SPACE;
        bySpace = new MapperBySpace<Current, Mapped>(otherSpace).bySpace(mappingField);
        return this;
    }

    @SafeVarargs
    final Mapper<Current, Mapped> byIndex(MetaClass<Mapped> otherSpace, MetaField<? extends MetaClass<Current>, ?>... indexedFields) {
        mode = INDEX;
        bySpace = new MapperBySpace<Current, Mapped>(otherSpace).byIndex(indexedFields);
        return this;
    }

    Mapper<Current, Mapped> byFunction(MetaMethod<MetaClass<? extends Storage>, Mapped> function) {
        mode = FUNCTION;
        byFunction = new MapperByFunction<>(function);
        return this;
    }
}

