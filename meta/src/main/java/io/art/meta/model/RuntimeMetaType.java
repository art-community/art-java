package io.art.meta.model;

import io.art.core.collection.*;
import io.art.meta.exception.*;
import io.art.meta.validator.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.model.MetaType.*;
import static io.art.meta.state.MetaComputationState.*;
import static java.text.MessageFormat.*;
import static java.util.Arrays.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class RuntimeMetaType {
    private final static Map<Type, MetaType<?>> CACHE = map();

    public static <T> MetaType<T> defineMetaType() {
        TypeReference<T> typeReference = new TypeReference<>() {
        };
        return defineMetaType(typeReference.type);
    }

    public static <T> MetaType<T> defineMetaType(Type type) {
        return cast(putIfAbsent(CACHE, type, () -> createMetaType(type)));
    }

    public static <T> MetaType<T> createMetaType(Class<T> type, MetaType<?>... parameters) {
        return computeMetaType(metaType(type, parameters));
    }

    public static <T extends Enum<T>> MetaType<T> createEnumMetaType(Class<T> type, Function<String, T> enumFactory) {
        return computeMetaType(metaEnum(type, cast(enumFactory)));
    }

    public static <T> MetaType<T> createArrayMetaType(Class<T> type, Function<Integer, ?> arrayFactory, MetaType<?> arrayComponentType) {
        return computeMetaType(metaArray(type, arrayFactory, arrayComponentType));
    }

    private static <T> MetaType<T> computeMetaType(MetaType<T> metaType) {
        metaType.beginComputation();
        ImmutableArray<ValidationResult> validationErrors = getValidationErrors();
        if (validationErrors.isEmpty()) {
            metaType.completeComputation();
            return cast(metaType);
        }
        StringBuilder validationErrorMessage = new StringBuilder(META_COMPUTATION_FAILED);
        validationErrors.forEach(error -> validationErrorMessage.append(error.getMessage()).append(NEW_LINE));
        throw new MetaException(validationErrorMessage.toString());
    }

    private static <T> MetaType<T> createMetaType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type owner = parameterizedType.getRawType();
            MetaType<?>[] parameters = stream(parameterizedType.getActualTypeArguments())
                    .map(RuntimeMetaType::defineMetaType)
                    .toArray(MetaType[]::new);
            return createMetaType(cast(owner), parameters);
        }
        if (type instanceof Class<?>) {
            Class<?> asClass = (Class<?>) type;
            if (asClass.isArray()) {
                Function<Integer, ?> factory = size -> cast(wrapExceptionCall(() -> asClass.getConstructors()[0].newInstance(size)));
                return cast(createArrayMetaType(asClass, factory, defineMetaType(asClass.getComponentType())));
            }
            if (asClass.isEnum()) {
                Function<String, Object> enumFactory = name -> {
                    for (Object value : asClass.getEnumConstants()) {
                        Enum<?> enumValue = (Enum<?>) value;
                        if (name.equals(enumValue.name())) {
                            return enumValue;
                        }
                    }
                    throw new MetaException(format(UNKOWN_RUNTIME_META_TYPE_CLASS, type));
                };
                return computeMetaType(metaEnum(asClass, cast(enumFactory)));
            }
            return computeMetaType(metaType(cast(type)));
        }
        if (type instanceof WildcardType) {
            return defineMetaType(((WildcardType) type).getUpperBounds()[0]);
        }
        throw new MetaException(format(UNKOWN_RUNTIME_META_TYPE_CLASS, type));
    }
}
