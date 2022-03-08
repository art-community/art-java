package io.art.storage;

import io.art.meta.model.*;
import lombok.*;

@Getter
public class FilterByFunction<Type> extends FilterRule<Type> {
    private final MetaMethod<MetaClass<? extends Storage>, Boolean> function;

    public FilterByFunction(Filter<Type> owner, MetaMethod<MetaClass<? extends Storage>, Boolean> function) {
        super(owner);
        this.function = function;
    }
}
