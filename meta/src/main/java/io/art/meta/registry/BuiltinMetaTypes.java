package io.art.meta.registry;

import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.meta.Meta.*;

@UtilityClass
public class BuiltinMetaTypes {
    public static MetaType<Long> LONG_PRIMITIVE_TYPE;
    public static MetaType<Long> LONG_TYPE;

    public static void initializeBuiltinMetaTypes() {
        LONG_PRIMITIVE_TYPE = definition(long.class);
        LONG_TYPE = definition(Long.class);
    }
}
