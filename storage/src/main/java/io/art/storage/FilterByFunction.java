package io.art.storage;

import io.art.meta.model.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class FilterByFunction<Type> {
    private final Filter<Type> owner;
    private final MetaMethod<MetaClass<? extends Storage>, Boolean> function;

    public Filter<Type> and() {
        return owner.and();
    }

    public Filter<Type> or() {
        return owner.or();
    }
}
