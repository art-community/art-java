package io.art.meta.model;

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
    private final InstanceMetaMethod<Object, ?> getter;
    private final int index;
}
