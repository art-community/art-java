package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class FilterByFunction<Type> {
    @Delegate
    private final FilterRule<Type> rule;
    @Getter
    private final MetaMethod<MetaClass<? extends Storage>, Boolean> function;
}
