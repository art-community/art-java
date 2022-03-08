package io.art.storage;

import io.art.storage.Filter.*;
import lombok.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class NestedFilter<Type> {
    protected final FilterRule<Type> rule;
    private final List<FilterPart> parts;

    public Filter<Type> and() {
        return rule.and();
    }

    public Filter<Type> or() {
        return rule.or();
    }
}
