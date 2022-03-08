package io.art.storage;

import lombok.*;
import java.util.function.*;

@RequiredArgsConstructor
public class FilterRule<Type> {
    private final Filter<Type> owner;

    public Filter<Type> and() {
        return owner.and();
    }

    public Filter<Type> or() {
        return owner.or();
    }

    public NestedFilter<Type> or(UnaryOperator<Filter<Type>> nested) {
        return owner.or(nested);
    }

    public NestedFilter<Type> and(UnaryOperator<Filter<Type>> nested) {
        return owner.and(nested);
    }
}
