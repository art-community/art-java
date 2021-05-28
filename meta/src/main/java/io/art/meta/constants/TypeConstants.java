package io.art.meta.constants;

import io.art.core.collection.*;
import io.art.value.constants.ValueModuleConstants.ValueType.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.BOOL;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.BYTE;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.FLOAT;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.INT;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.LONG;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.STRING;
import java.lang.reflect.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public interface TypeConstants {
    ImmutableSet<Type> PRIMITIVE_TYPES = immutableSetOf(
            String.class,
            char.class,
            int.class,
            short.class,
            long.class,
            boolean.class,
            double.class,
            byte.class,
            float.class,
            Character.class,
            Integer.class,
            Short.class,
            Long.class,
            Boolean.class,
            Double.class,
            Byte.class,
            Float.class,
            UUID.class,
            LocalDateTime.class,
            ZonedDateTime.class,
            Duration.class,
            Date.class
    );

    ImmutableMap<Class<?>, PrimitiveType> PRIMITIVE_TYPE_MAPPINGS = ImmutableMap.<Class<?>, PrimitiveType>immutableMapBuilder()
            .put(char.class, STRING)
            .put(int.class, INT)
            .put(short.class, INT)
            .put(long.class, LONG)
            .put(boolean.class, BOOL)
            .put(double.class, FLOAT)
            .put(byte.class, BYTE)
            .put(float.class, FLOAT)
            .build();

    ImmutableSet<Class<?>> COLLECTION_TYPES = immutableSetOf(
            List.class,
            Queue.class,
            Deque.class,
            Set.class,
            Collection.class,
            Stream.class,
            ImmutableArray.class,
            ImmutableSet.class
    );
}
