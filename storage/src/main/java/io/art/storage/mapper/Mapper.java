package io.art.storage.mapper;

import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.constants.StorageConstants.*;
import lombok.*;
import static io.art.storage.constants.StorageConstants.MappingMode.*;

@Getter
public class Mapper<Current, Mapped> {
    private MappingMode mode;
    private MapperByField<?> byField;
    private MapperBySpace<?, ?> bySpace;
    private MapperBySpace<?, ?> byIndex;
    private MapperByFunction<?> byFunction;

    public Mapper<Current, Mapped> byField(MetaField<? extends MetaClass<Current>, Mapped> field) {
        mode = FIELD;
        byField = new MapperByField<>(field);
        return this;
    }

    public Mapper<Current, Mapped> bySpace(MetaClass<Mapped> otherSpace, MetaField<? extends MetaClass<Current>, ?> mappingField) {
        mode = SPACE;
        bySpace = new MapperBySpace<Current, Mapped>(otherSpace).bySpace(mappingField);
        return this;
    }

    @SafeVarargs
    public final Mapper<Current, Mapped> byIndex(MetaClass<Mapped> otherSpace, MetaField<? extends MetaClass<Current>, ?>... indexedFields) {
        mode = INDEX;
        bySpace = new MapperBySpace<Current, Mapped>(otherSpace).byIndex(indexedFields);
        return this;
    }

    public Mapper<Current, Mapped> byFunction(MetaMethod<? extends MetaClass<? extends Storage>, Mapped> function) {
        mode = FUNCTION;
        byFunction = new MapperByFunction<>(function);
        return this;
    }
}

