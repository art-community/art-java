package io.art.meta.model;

import io.art.core.annotation.*;
import lombok.*;

@Getter
@Public
@AllArgsConstructor
public class TypedObject {
    private final MetaType<?> type;
    private final Object object;

    public static TypedObject typed(MetaType<?> type, Object object) {
        return new TypedObject(type, object);
    }
}
