package io.art.storage;

import io.art.meta.model.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class MapperByFunction<Type> {
    private final MetaMethod<MetaClass<? extends Storage>, Type> function;
}
