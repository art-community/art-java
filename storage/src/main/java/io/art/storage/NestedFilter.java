package io.art.storage;

import io.art.storage.Filter.*;
import lombok.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class NestedFilter<Type> {
    private final List<FilterPart> parts;
}
