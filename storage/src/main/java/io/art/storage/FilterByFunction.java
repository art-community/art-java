package io.art.storage;

import io.art.meta.model.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class FilterByFunction<Type> {
    private final FilterRule<Type> rule;
    private final MetaMethod<MetaClass<? extends Storage>, Boolean> function;

    public Filter<Type> and() {
        return rule.and();
    }

    public Filter<Type> or() {
        return rule.or();
    }
}
