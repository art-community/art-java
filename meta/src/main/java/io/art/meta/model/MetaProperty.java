package io.art.meta.model;

import io.art.core.collection.*;
import lombok.*;
import lombok.experimental.*;

@Getter
@ToString
@Builder(toBuilder = true)
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MetaProperty<T> {
    @EqualsAndHashCode.Include
    private final String name;
    private final MetaType<T> type;
    private final MetaClass<T> owner;
    private final ImmutableArray<MetaConstructor<?>> constructors;
    private final InstanceMetaMethod<Object, T> getter;
    private final MetaMethod<Void> setter;
}
