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
public class TarantoolSpaceConfigurator<C> {
    private final Class<? extends Storage> storageClass;
    private final Class<C> spaceClass;
    private Supplier<MetaField<? extends MetaClass<C>, ?>> field;
    private Class<? extends Indexes<C>> indexes;
    private ShardFunction shardFunction;

    public <M extends MetaClass<C>> TarantoolSpaceConfigurator<C> id(Supplier<MetaField<M, ?>> field) {
        this.field = cast(field);
        return this;
    }

    public <I extends Indexes<C>> TarantoolSpaceConfigurator<C> indexes(Class<I> indexes) {
        this.indexes = indexes;
        return this;
    }

    public <P1> TarantoolSpaceConfigurator<C> sharded(ShardFunction1<C, P1> operator) {
        shardFunction = parameters -> operator.shard(cast(parameters.values().get(0)));
        return this;
    }
}
