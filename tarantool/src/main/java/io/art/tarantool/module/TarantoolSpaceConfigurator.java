package io.art.tarantool.module;

import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter(value = PACKAGE)
@RequiredArgsConstructor
public class TarantoolSpaceConfigurator<SpaceType> {
    private final Class<SpaceType> spaceClass;
    private Supplier<MetaField<? extends MetaClass<SpaceType>, ?>> id;
    private Class<? extends Indexes<SpaceType>> indexes;
    private Class<? extends Sharders<SpaceType>> sharders;

    public <M extends MetaClass<SpaceType>> TarantoolSpaceConfigurator<SpaceType> id(Supplier<MetaField<M, ?>> field) {
        this.id = cast(field);
        return this;
    }

    public <I extends Indexes<SpaceType>> TarantoolSpaceConfigurator<SpaceType> indexes(Class<I> indexes) {
        this.indexes = indexes;
        return this;
    }

    public <S extends Sharders<SpaceType>> TarantoolSpaceConfigurator<SpaceType> sharders(Class<S> sharders) {
        this.sharders = sharders;
        return this;
    }
}
