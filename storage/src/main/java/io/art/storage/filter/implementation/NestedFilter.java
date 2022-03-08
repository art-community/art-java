package io.art.storage.filter.implementation;

import io.art.storage.filter.implementation.FilterImplementation.*;
import lombok.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class NestedFilter {
    private final List<FilterPart> parts;
}
