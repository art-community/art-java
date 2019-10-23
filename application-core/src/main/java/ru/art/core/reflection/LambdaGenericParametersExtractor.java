package ru.art.core.reflection;

import lombok.experimental.*;
import ru.art.core.exception.*;
import static java.lang.Class.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.ReflectionConstants.*;
import static ru.art.core.constants.StringConstants.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

@UtilityClass
public class LambdaGenericParametersExtractor {
    public static <T> Class<T> extractFirstLambdaGenericParameters(Object lambda) {
        return cast(extractLambdaGenericParameters(lambda).get(0));
    }

    public static List<Class<?>> extractLambdaGenericParameters(Object lambda) {
        try {
            Method writeReplace = lambda.getClass().getDeclaredMethod(WRITE_REPLACE);
            writeReplace.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(lambda);
            String[] classes = serializedLambda.getImplMethodSignature().split(SEMICOLON);
            return stream(classes)
                    .map(LambdaGenericParametersExtractor::extractClass)
                    .limit(classes.length - 1)
                    .collect(toList());
        } catch (Throwable throwable) {
            throw new InternalRuntimeException(throwable);
        }
    }

    private static Class<?> extractClass(String parameterClass) {
        try {
            return forName(parameterClass.substring(parameterClass.indexOf(CLASS_PREFIX) + 1).replaceAll(SLASH, DOT));
        } catch (Throwable throwable) {
            throw new InternalRuntimeException(throwable);
        }
    }
}
