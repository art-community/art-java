/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.generator.mapper.operations;

import com.squareup.javapoet.*;
import ru.art.generator.mapper.annotation.*;
import ru.art.generator.mapper.exception.*;
import static java.text.MessageFormat.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.mapper.constants.Constants.*;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.*;
import static ru.art.generator.mapper.constants.ExceptionConstants.MapperGeneratorExceptions.*;
import static ru.art.generator.mapper.operations.GeneratorOperations.*;
import java.lang.reflect.Field;
import java.lang.reflect.*;

/**
 * Interface containing common static methods which can be used in other operations
 */
public interface CommonOperations {

    /**
     * Wrap of System.out.println.
     *
     * @param message - text to print.
     */
    static void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Wrap of System.err.println.
     *
     * @param errorText - error to print.
     */
    static void printError(String errorText) {
        System.err.println(errorText);
    }

    /**
     * Generate mapper for class, if it wasn't generated earlier.
     *
     * @param genClass      - class of mapper's model.
     * @param jarPathToMain - classpath from root to main.
     * @return ClassName of new generated class.
     */
    static ClassName createMapperForInnerClassIfNeeded(Class<?> genClass, String jarPathToMain) {
        if (genClass.isEnum()) return ClassName.get(genClass);
        if (!genClass.isAnnotationPresent(NonGenerated.class)) {
            String classPackage = genClass.getName().substring(0, genClass.getName().indexOf(genClass.getSimpleName()) - 1);
            String genPackage = classPackage.contains(MODEL) ?
                    classPackage.replace(MODEL, MAPPING) :
                    classPackage.substring(0, classPackage.lastIndexOf(DOT)) + DOT + MAPPING;
            if (!generatedFiles.contains(genClass))
                createMapperClass(genClass, genPackage, jarPathToMain);
            return ClassName.get(genPackage, genClass.getSimpleName() + MAPPER);
        }
        throw new InnerClassGenerationException(format(UNABLE_TO_CREATE_INNER_CLASS_MAPPER, genClass));
    }

    /**
     * Generate mapper for class, if it wasn't generated earlier.
     *
     * @param field         - field which type is a model for new mapper.
     * @param jarPathToMain - classpath from root to main.
     * @return ClassName of new generated class.
     */
    static ClassName createMapperForInnerClassIfNeeded(Field field, String jarPathToMain) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> genClass = (Class) type.getActualTypeArguments()[0];
        if (genClass.isEnum()) return ClassName.get(genClass);
        if (!genClass.isAnnotationPresent(NonGenerated.class)) {
            String classPackage = genClass.getName().substring(0, genClass.getName().indexOf(genClass.getSimpleName()) - 1);
            String genPackage = classPackage.contains(MODEL) ?
                    classPackage.replace(MODEL, MAPPING) :
                    classPackage.substring(0, classPackage.lastIndexOf(DOT)) + DOT + MAPPING;
            if (!generatedFiles.contains(genClass))
                createMapperClass(genClass, genPackage, jarPathToMain);
            return ClassName.get(genPackage, genClass.getSimpleName() + MAPPER);
        }
        throw new InnerClassGenerationException(format(UNABLE_TO_CREATE_INNER_CLASS_MAPPER, genClass));
    }
}
