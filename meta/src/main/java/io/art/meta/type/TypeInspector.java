package io.art.meta.type;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.core.source.*;
import io.art.meta.exception.*;
import io.art.value.immutable.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.constants.TypeConstants.*;
import static io.art.meta.type.TypeSubstitutor.*;
import static java.text.MessageFormat.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

@UtilityClass
public class TypeInspector {
    public static boolean isBoolean(Type fieldType) {
        return fieldType == boolean.class;
    }

    public static boolean isFlux(Type fieldType) {
        return extractClass(fieldType).isAssignableFrom(Flux.class);
    }

    public static boolean isMono(Type fieldType) {
        return extractClass(fieldType).isAssignableFrom(Mono.class);
    }

    public static boolean isLazyValue(Type fieldType) {
        return LazyProperty.class == extractClass(fieldType);
    }

    public static boolean isOptional(Type fieldType) {
        return Optional.class == extractClass(fieldType);
    }

    public static boolean isCollection(Type type) {
        return COLLECTION_TYPES.stream().anyMatch(collection -> collection.isAssignableFrom(extractClass(type)));
    }

    public static boolean isObject(Type type) {
        return type == Object.class;
    }

    public static boolean isList(Type type) {
        return List.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isSet(Type type) {
        return Set.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isQueue(Type type) {
        return Queue.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isStream(Type type) {
        return Stream.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isDequeue(Type type) {
        return Deque.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isMap(Type type) {
        return Map.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isImmutableArray(Type type) {
        return ImmutableArray.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isImmutableSet(Type type) {
        return ImmutableSet.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isImmutableMap(Type type) {
        return ImmutableMap.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isImmutableCollection(Type type) {
        return ImmutableMap.class.isAssignableFrom(extractClass(type))
                || ImmutableSet.class.isAssignableFrom(extractClass(type))
                || ImmutableArray.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isPrimitive(Type type) {
        return PRIMITIVE_TYPES.contains(type);
    }

    public static boolean isNotPrimitive(Type type) {
        return !isPrimitive(type);
    }

    public static boolean isClass(Type type) {
        return type instanceof Class;
    }

    public static boolean isParametrized(Type type) {
        return type instanceof ParameterizedType;
    }

    public static boolean isGenericArray(Type type) {
        return type instanceof GenericArrayType;
    }

    public static boolean isWildcard(Type type) {
        return type instanceof WildcardType;
    }

    public static boolean isVariable(Type type) {
        return type instanceof TypeVariable;
    }

    public static boolean isValue(Type type) {
        return Value.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isNestedConfiguration(Type type) {
        return NestedConfiguration.class.isAssignableFrom(extractClass(type));
    }

    public static boolean isByteArray(Type type) {
        return byte[].class.equals(type);
    }

    public static boolean isArray(Type type) {
        return isGenericArray(type) || extractClass(type).isArray();
    }

    public static boolean isPrimitiveVoid(Type type) {
        return extractClass(type) == void.class;
    }

    public static boolean isVoid(Type type) {
        Class<?> voidClass = extractClass(type);
        return voidClass == void.class || voidClass == Void.class;
    }

    public static Class<?> extractClass(ParameterizedType parameterizedType) {
        return extractClass(parameterizedType.getRawType());
    }

    public static Class<?> extractClass(GenericArrayType genericArrayType) {
        return extractClass(genericArrayType.getGenericComponentType());
    }

    public static Class<?> extractClass(Type type) {
        if (isClass(type)) {
            return (Class<?>) type;
        }
        if (isParametrized(type)) {
            return extractClass((ParameterizedType) type);
        }
        if (isGenericArray(type)) {
            return extractClass((GenericArrayType) type);
        }
        if (isWildcard(type)) {
            return extractClass(substituteWildcard((WildcardType) type));
        }
        throw new MetaException(format(UNSUPPORTED_TYPE, type));
    }
}
