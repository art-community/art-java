package io.art.core.model;

import lombok.*;
import lombok.experimental.*;
import static io.art.core.factory.ArrayFactory.*;
import java.util.*;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class Tuple1<T1> implements Tuple {
    private final T1 value1;

    @Override
    public List<Object> values() {
        return fixedArrayOf(value1);
    }
}
