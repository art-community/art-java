package io.art.meta.registry;

import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.meta.Meta.*;

@UtilityClass
public class BuiltinMetaTypes {
    private static MetaType<Long> LONG_PRIMITIVE_TYPE;
    private static MetaType<Long> LONG_TYPE;
    private static MetaType<Integer> INTEGER_PRIMITIVE_TYPE;
    private static MetaType<Integer> INTEGER_TYPE;
    private static MetaType<Boolean> BOOLEAN_TYPE;
    private static MetaType<Boolean> BOOLEAN_PRIMITIVE_TYPE;

    public static void initializeBuiltinMetaTypes() {
        LONG_PRIMITIVE_TYPE = definition(long.class);
        LONG_TYPE = definition(Long.class);
        INTEGER_PRIMITIVE_TYPE = definition(int.class);
        INTEGER_TYPE = definition(Integer.class);
        BOOLEAN_PRIMITIVE_TYPE = definition(boolean.class);
        BOOLEAN_TYPE = definition(Boolean.class);
    }

    public static MetaType<Long> longPrimitiveType() {
        return LONG_PRIMITIVE_TYPE;
    }

    public static MetaType<Long> longType() {
        return LONG_TYPE;
    }

    public static MetaType<Integer> integerPrimitiveType() {
        return INTEGER_PRIMITIVE_TYPE;
    }

    public static MetaType<Integer> integerType() {
        return INTEGER_TYPE;
    }

    public static MetaType<Boolean> booleanPrimitiveType() {
        return BOOLEAN_PRIMITIVE_TYPE;
    }

    public static MetaType<Boolean> booleanType() {
        return BOOLEAN_TYPE;
    }
}
