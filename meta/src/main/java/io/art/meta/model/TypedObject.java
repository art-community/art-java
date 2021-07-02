package io.art.meta.model;

import lombok.*;

@Getter
@AllArgsConstructor
public class TypedObject {
    private final MetaType<?> type;
    private final Object object;

    public static TypedObject typed(MetaType<?> type, Object object) {
        return new TypedObject(type, object);
    }
}
