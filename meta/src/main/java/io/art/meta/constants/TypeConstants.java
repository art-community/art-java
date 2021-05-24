package io.art.meta.constants;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.core.source.*;
import io.art.value.constants.ValueModuleConstants.ValueType.*;
import io.art.value.immutable.*;
import reactor.core.publisher.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.BOOL;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.BYTE;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.DOUBLE;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.FLOAT;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.INT;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.LONG;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.STRING;
import java.lang.reflect.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public interface TypeConstants {
    ImmutableSet<Class<?>> CORE_BASE_TYPES = immutableSetOf(
            List.class,
            Collection.class,
            Set.class,
            Queue.class,
            Deque.class,
            Map.class,
            Flux.class,
            Mono.class,
            Date.class,
            Stream.class,
            ImmutableArray.class,
            ImmutableSet.class,
            ImmutableMap.class,
            Value.class,
            NestedConfiguration.class
    );

    ImmutableSet<Class<?>> CORE_TYPES = immutableSetOf(
            void.class,
            Void.class,
            String.class,
            boolean.class,
            Boolean.class,
            short.class,
            Short.class,
            char.class,
            Character.class,
            int.class,
            Integer.class,
            long.class,
            Long.class,
            byte.class,
            Byte.class,
            float.class,
            Float.class,
            double.class,
            Double.class,
            UUID.class,
            Duration.class,
            LocalDateTime.class,
            ZonedDateTime.class,
            Object.class,
            LazyProperty.class,
            Optional.class
    );

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

    ImmutableSet<Type> JAVA_PRIMITIVE_TYPES = immutableSetOf(
            char.class,
            int.class,
            short.class,
            long.class,
            boolean.class,
            double.class,
            byte.class,
            float.class
    );

    ImmutableMap<Class<?>, Class<?>> JAVA_PRIMITIVE_MAPPINGS = ImmutableMap.<Class<?>, Class<?>>immutableMapBuilder()
            .put(char.class, Character.class)
            .put(int.class, Integer.class)
            .put(short.class, Short.class)
            .put(long.class, Long.class)
            .put(boolean.class, Boolean.class)
            .put(double.class, Double.class)
            .put(byte.class, Byte.class)
            .put(float.class, Float.class)
            .build();

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

    ImmutableMap<Type, PrimitiveType> JAVA_TO_PRIMITIVE_TYPE = ImmutableMap.<Type, PrimitiveType>immutableMapBuilder()
            .put(char.class, STRING)
            .put(int.class, INT)
            .put(short.class, INT)
            .put(long.class, LONG)
            .put(boolean.class, BOOL)
            .put(double.class, DOUBLE)
            .put(byte.class, BYTE)
            .put(float.class, FLOAT)
            .put(Character.class, STRING)
            .put(Integer.class, INT)
            .put(Short.class, INT)
            .put(Long.class, LONG)
            .put(Boolean.class, BOOL)
            .put(Double.class, DOUBLE)
            .put(Byte.class, BYTE)
            .put(Float.class, FLOAT)
            .build();


}
