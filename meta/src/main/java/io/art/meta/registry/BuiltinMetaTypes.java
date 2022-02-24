package io.art.meta.registry;

import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.meta.Meta.*;

@UtilityClass
public class BuiltinMetaTypes {
    private static MetaType<Long> LONG_PRIMITIVE_TYPE;
    private static MetaType<Long> LONG_TYPE;

    public static void initializeBuiltinMetaTypes() {
        LONG_PRIMITIVE_TYPE = definition(long.class);
        LONG_TYPE = definition(Long.class);
    }

    public static MetaType<Long> longPrimitiveType() {
        return LONG_PRIMITIVE_TYPE;
    }

    public static MetaType<Long> longType() {
        return LONG_TYPE;
    }
}
