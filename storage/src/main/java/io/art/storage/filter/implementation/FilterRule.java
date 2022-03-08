package io.art.storage.filter.implementation;

import io.art.storage.filter.model.*;
import lombok.*;
import java.util.function.*;

@RequiredArgsConstructor
public class FilterRule<Type> {
    private final FilterImplementation<Type> owner;

    public Filter<Type> and() {
        return owner.and();
    }

    public Filter<Type> or() {
        return owner.or();
    }

    public Filter<Type> or(Consumer<Filter<Type>> nested) {
        owner.or(nested);
        return owner;
    }

    public Filter<Type> and(Consumer<Filter<Type>> nested) {
        owner.and(nested);
        return owner;
    }
}
