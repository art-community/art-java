package io.art.storage.mapper;

import io.art.meta.model.*;
import io.art.storage.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class MapperByFunction<Type> {
    private final MetaMethod<MetaClass<? extends Storage>, Type> function;
}
