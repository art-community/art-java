/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.core.reflection;

import lombok.experimental.*;
import io.art.core.exception.*;
import static java.lang.Class.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static io.art.core.constants.ReflectionConstants.*;
import static io.art.core.constants.StringConstants.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

@UtilityClass
public class LambdaGenericParametersExtractor {
    public static <T> Optional<Class<?>> extractFirstLambdaGenericParameters(Object lambda) {
        return extractLambdaGenericParameters(lambda).stream().findFirst();
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

    public static Optional<? extends Class<?>> extractLambdaReturnValue(Object lambda) {
        try {
            Method writeReplace = lambda.getClass().getDeclaredMethod(WRITE_REPLACE);
            writeReplace.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(lambda);
            String[] classes = serializedLambda.getImplMethodSignature().split(SEMICOLON);
            return stream(classes)
                    .map(LambdaGenericParametersExtractor::extractClass)
                    .skip(classes.length - 1)
                    .findFirst();
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
