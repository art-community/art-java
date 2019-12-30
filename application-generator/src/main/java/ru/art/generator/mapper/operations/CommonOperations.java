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
import ru.art.generator.mapper.models.GenerationPackageModel;

import static java.io.File.separator;
import static java.text.MessageFormat.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.mapper.constants.Constants.*;
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
     * @param generationInfo - information about packages and path for generated class.
     * @return ClassName of new generated class.
     */
    static ClassName createMapperForInnerClassIfNeeded(Class<?> genClass, GenerationPackageModel generationInfo) {
        if (genClass.isEnum()) return ClassName.get(genClass);
        String packageName = genClass.getPackage().getName().equals(generationInfo.getModelPackage())
                ? EMPTY_STRING
                :genClass.getPackage().getName().replace(generationInfo.getModelPackage() + DOT, EMPTY_STRING);
        if (!genClass.isAnnotationPresent(IgnoreGeneration.class)) {
            if (!generatedFiles.contains(genClass.getName())) {
                GenerationPackageModel generationInnerClassInfo = GenerationPackageModel.builder()
                        .startPackage(generationInfo.getModelPackage())
                        .startPackagePath(generationInfo.getModelPackagePath())
                        .startPackagePathCompiled(generationInfo.getModelPackagePathCompiled())
                        .modelPackage(genClass.getPackage().getName())
                        .modelPackagePath(isEmpty(packageName)
                                ? generationInfo.getModelPackagePath()
                                : generationInfo.getModelPackagePath() + separator + packageName)
                        .modelPackagePathCompiled(isEmpty(packageName)
                                ? generationInfo.getModelPackagePathCompiled()
                                : generationInfo.getModelPackagePathCompiled() + separator + packageName)
                        .genPackage(isEmpty(packageName)
                                ? generationInfo.getGenPackage()
                                : generationInfo.getGenPackage() + DOT + packageName)
                        .genPackagePath(isEmpty(packageName)
                                ? generationInfo.getGenPackagePath()
                                : generationInfo.getGenPackagePath() + separator + packageName)
                        .genPackagePathCompiled(isEmpty(packageName)
                                ? generationInfo.getGenPackagePathCompiled()
                                : generationInfo.getGenPackagePathCompiled() + separator + packageName)
                        .jarPathToMain(generationInfo.getJarPathToMain())
                        .build();
                createMapperClass(genClass, generationInnerClassInfo);
                return ClassName.get(generationInnerClassInfo.getGenPackage(), genClass.getSimpleName() + MAPPER);
            }
            String newPackageName = isEmpty(packageName) ? generationInfo.getGenPackage() : generationInfo.getGenPackage() + DOT + packageName;
            return ClassName.get(newPackageName, genClass.getSimpleName() + MAPPER);
        }
        throw new InnerClassGenerationException(format(UNABLE_TO_CREATE_INNER_CLASS_MAPPER, genClass));
    }

    /**
     * Gets class by field.
     * @param field - field which type is a model for new mapper.
     * @return class from field
     */
    static Class<?> getClassFromField (Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return (Class) type.getActualTypeArguments()[0];
    }
}
