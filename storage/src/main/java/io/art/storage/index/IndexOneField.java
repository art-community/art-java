package io.art.storage.index;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import java.util.*;

@RequiredArgsConstructor
public class IndexOneField<C, F1> implements Index1<C, F1> {
    private final MetaField<? extends MetaClass<C>, F1> field1;

    @Override
    public List<MetaField<?, ?>> fields() {
        return linkedListOf(field1);
    }
}
