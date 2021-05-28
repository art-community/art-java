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
    public boolean isBoolean(Type fieldType) {
        return fieldType == boolean.class;
    }

    public boolean isFlux(Type fieldType) {
        return extractClass(fieldType).isAssignableFrom(Flux.class);
    }

    public boolean isMono(Type fieldType) {
        return extractClass(fieldType).isAssignableFrom(Mono.class);
    }

    public boolean isLazyValue(Type fieldType) {
        return LazyProperty.class == extractClass(fieldType);
    }

    public boolean isOptional(Type fieldType) {
        return Optional.class == extractClass(fieldType);
    }

    public boolean isCollection(Type type) {
        return COLLECTION_TYPES.stream().anyMatch(collection -> collection.isAssignableFrom(extractClass(type)));
    }

    public boolean isObject(Type type) {
        return type == Object.class;
    }

    public boolean isList(Type type) {
        return List.class.isAssignableFrom(extractClass(type));
    }

    public boolean isSet(Type type) {
        return Set.class.isAssignableFrom(extractClass(type));
    }

    public boolean isQueue(Type type) {
        return Queue.class.isAssignableFrom(extractClass(type));
    }

    public boolean isStream(Type type) {
        return Stream.class.isAssignableFrom(extractClass(type));
    }

    public boolean isDequeue(Type type) {
        return Deque.class.isAssignableFrom(extractClass(type));
    }

    public boolean isMap(Type type) {
        return Map.class.isAssignableFrom(extractClass(type));
    }

    public boolean isImmutableArray(Type type) {
        return ImmutableArray.class.isAssignableFrom(extractClass(type));
    }

    public boolean isImmutableSet(Type type) {
        return ImmutableSet.class.isAssignableFrom(extractClass(type));
    }

    public boolean isImmutableMap(Type type) {
        return ImmutableMap.class.isAssignableFrom(extractClass(type));
    }

    public boolean isImmutableCollection(Type type) {
        return ImmutableMap.class.isAssignableFrom(extractClass(type))
                || ImmutableSet.class.isAssignableFrom(extractClass(type))
                || ImmutableArray.class.isAssignableFrom(extractClass(type));
    }

    public boolean isPrimitive(Type type) {
        return PRIMITIVE_TYPES.contains(type);
    }

    public boolean isClass(Type type) {
        return type instanceof Class;
    }

    public boolean isParametrized(Type type) {
        return type instanceof ParameterizedType;
    }

    public boolean isGenericArray(Type type) {
        return type instanceof GenericArrayType;
    }

    public boolean isWildcard(Type type) {
        return type instanceof WildcardType;
    }

    public boolean isVariable(Type type) {
        return type instanceof TypeVariable;
    }

    public boolean isValue(Type type) {
        return Value.class.isAssignableFrom(extractClass(type));
    }

    public boolean isNestedConfiguration(Type type) {
        return NestedConfiguration.class.isAssignableFrom(extractClass(type));
    }

    public boolean isByteArray(Type type) {
        return byte[].class.equals(type);
    }

    public boolean isArray(Type type) {
        return isGenericArray(type) || extractClass(type).isArray();
    }

    public boolean isNotPrimitive(Type type) {
        return !isPrimitive(type);
    }

    public boolean isVoid(Type type) {
        return extractClass(type) == void.class;
    }

    public Class<?> extractClass(ParameterizedType parameterizedType) {
        return extractClass(parameterizedType.getRawType());
    }

    public Class<?> extractClass(GenericArrayType genericArrayType) {
        return extractClass(genericArrayType.getGenericComponentType());
    }

    public Class<?> extractClass(Type type) {
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
