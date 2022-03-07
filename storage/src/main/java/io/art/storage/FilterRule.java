package io.art.storage;

import lombok.*;

@RequiredArgsConstructor
public class FilterRule<Type> {
    private final Filter<Type> owner;

    public Filter<Type> and() {
        return owner.and();
    }

    public Filter<Type> or() {
        return owner.or();
    }
}
