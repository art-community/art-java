package io.art.storage;

import io.art.meta.model.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class MapperByField<Type> {
    private final MetaField<? extends MetaClass<Type>, ?> field;
}
