package io.art.core.model;

import lombok.*;
import lombok.experimental.*;
import static io.art.core.factory.ArrayFactory.*;
import java.util.*;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class Tuple4<T1, T2, T3, T4> implements Tuple {
    private final T1 value1;
    private final T2 value2;
    private final T3 value3;
    private final T4 value4;

    @Override
    public List<Object> values() {
        return fixedArrayOf(value1, value2, value3, value4);
    }
}
