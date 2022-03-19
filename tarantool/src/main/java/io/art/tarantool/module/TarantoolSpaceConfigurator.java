package io.art.tarantool.module;

import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter(value = PACKAGE)
@RequiredArgsConstructor
public class TarantoolSpaceConfigurator<ModelType> {
    private final Class<? extends Storage> storageClass;
    private final Class<ModelType> spaceClass;
    private Supplier<MetaField<? extends MetaClass<ModelType>, ?>> field;
    private Class<? extends Indexes<ModelType>> indexes;
    private Class<? extends Sharders<ModelType>> sharders;
    private boolean router;

    public <M extends MetaClass<ModelType>> TarantoolSpaceConfigurator<ModelType> id(Supplier<MetaField<M, ?>> field) {
        this.field = cast(field);
        return this;
    }

    public <I extends Indexes<ModelType>> TarantoolSpaceConfigurator<ModelType> indexes(Class<I> indexes) {
        this.indexes = indexes;
        return this;
    }

    public <S extends Sharders<ModelType>> TarantoolSpaceConfigurator<ModelType> router() {
        router = true;
        return this;
    }

    public <S extends Sharders<ModelType>> TarantoolSpaceConfigurator<ModelType> sharders(Class<S> sharders) {
        router = true;
        this.sharders = sharders;
        return this;
    }
}
