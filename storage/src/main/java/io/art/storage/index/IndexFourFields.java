package io.art.storage.index;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import java.util.*;

@RequiredArgsConstructor
public class IndexFourFields<C, F1, F2, F3, F4> implements Index4<C, F1, F2, F3, F4> {
    private final MetaField<? extends MetaClass<C>, F1> field1;
    private final MetaField<? extends MetaClass<C>, F2> field2;
    private final MetaField<? extends MetaClass<C>, F3> field3;
    private final MetaField<? extends MetaClass<C>, F4> field4;

    @Override
    public List<MetaField<?, ?>> fields() {
        return linkedListOf(field1, field2, field3, field4);
    }
}
