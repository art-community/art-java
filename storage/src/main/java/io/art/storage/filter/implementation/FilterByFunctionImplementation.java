package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.*;
import lombok.*;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class FilterByFunctionImplementation<Type> {
    @Delegate
    private final FilterRule<Type> rule;
    @Getter
    private final MetaMethod<? extends MetaClass<? extends Storage>, Boolean> function;
}
