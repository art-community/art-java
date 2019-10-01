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
import ru.art.core.checker.*;
import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.generator.mapper.annotation.*;
import ru.art.generator.mapper.exception.*;
import static com.squareup.javapoet.CodeBlock.*;
import static com.squareup.javapoet.TypeSpec.*;
import static java.io.File.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static javax.lang.model.element.Modifier.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.generator.mapper.constants.Constants.*;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.*;
import static ru.art.generator.mapper.constants.Constants.SupportedJavaClasses.*;
import static ru.art.generator.mapper.constants.Constants.SymbolsAndFormatting.*;
import static ru.art.generator.mapper.constants.ExceptionConstants.MapperGeneratorExceptions.*;
import static ru.art.generator.mapper.constants.FromModelConstants.*;
import static ru.art.generator.mapper.constants.ToModelConstants.*;
import static ru.art.generator.mapper.operations.CollectionGeneratorOperations.*;
import static ru.art.generator.mapper.operations.CommonOperations.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.util.*;
import java.util.stream.*;

/**
 * Interface containing static methods for creating mapper classes,
 * generation of ToModel and FromModel methods.
 */
public interface GeneratorOperations {

    Set<Class> generatedFiles = new HashSet<>();

    /**
     * Generate block ValueToModelMapper and ValueFromModelMapper for class.
     *
     * @param clazz         - class of model for mappers generating.
     * @param jarPathToMain - classpath from root to main.
     * @return TypeSpec containing ToModel and FromModel mappers.
     * @throws MappingGeneratorException is thrown when any other exception happens in attempt to create mapper.
     */
    static TypeSpec generateMappers(Class<?> clazz, String jarPathToMain)
            throws MappingGeneratorException {
        try {
            return interfaceBuilder(clazz.getSimpleName() + MAPPER)
                    .addModifiers(PUBLIC, STATIC)
                    .addFields(generateConstantsBlock(clazz))
                    .addField(generateToModelBlock(clazz, jarPathToMain))
                    .addField(generateFromModelBlock(clazz, jarPathToMain))
                    .build();
        } catch (Throwable e) {
            throw new MappingGeneratorException(format(UNABLE_TO_GENERATE_INTERFACE, clazz.getSimpleName(), e.getClass().getSimpleName()), e);
        }
    }

    /**
     * Creating ordinary mapping class.
     *
     * @param clazz         - class of model for mappers generating.
     * @param genPackage    - string value of mapper's package.
     * @param jarPathToMain - classpath from root to main.
     * @throws MappingGeneratorException is thrown when IOException or NullPointerException
     *                                   happens while writing to file.
     */
    static void createMapperClass(Class<?> clazz, String genPackage, String jarPathToMain)
            throws MappingGeneratorException {
        printMessage(format(START_GENERATING, clazz.getSimpleName() + MAPPER));
        for (int i = 0; i < clazz.getClasses().length; i++) {
            if (!clazz.getClasses()[i].getSimpleName().equals(clazz.getSimpleName() + BUILDER) &&
                    !generatedFiles.contains(clazz.getClasses()[i]) &&
                    !clazz.getClasses()[i].isAnnotationPresent(IgnoreGeneration.class) &&
                    !clazz.isEnum()) {
                createMapperClass(clazz.getClasses()[i], genPackage, jarPathToMain);
            }
        }

        TypeSpec mapperType = generateMappers(clazz, jarPathToMain);

        JavaFile javaFile = JavaFile.builder(genPackage, mapperType)
                .indent(TABULATION)
                .addStaticImport(CheckerForEmptiness.class, IS_NOT_EMPTY)
                .build();
        try {
            URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
            StringBuilder classJarPath = new StringBuilder();
            if (nonNull(location) && (classJarPath.append(location.getPath()).toString().contains(DOT_JAR))) {
                String temp = jarPathToMain.substring(0, jarPathToMain.substring(0, jarPathToMain.lastIndexOf(BUILD)).lastIndexOf(separator));
                classJarPath.replace(0, classJarPath.length(), temp.substring(0, temp.lastIndexOf(separator)))
                        .append(separator)
                        .append(BUILD)
                        .append(separator);
            }
            if (classJarPath.length() != 0) {
                javaFile.writeTo(new File(classJarPath.subSequence(0, classJarPath.indexOf(BUILD)).toString() + SRC_MAIN_JAVA));
                generatedFiles.add(clazz);
                printMessage(format(GENERATED_SUCCESSFULLY, clazz.getSimpleName() + MAPPER));
                return;
            }
            javaFile.writeTo(new File(jarPathToMain.subSequence(0, jarPathToMain.indexOf(BUILD)).toString() + SRC_MAIN_JAVA));
            generatedFiles.add(clazz);
            printMessage(format(GENERATED_SUCCESSFULLY, clazz.getSimpleName() + MAPPER));
        } catch (StringIndexOutOfBoundsException e) {
            throw new MappingGeneratorException(format(UNABLE_TO_PARSE_JAR_PATH, clazz.getSimpleName()), e);
        } catch (IOException e) {
            throw new MappingGeneratorException(format(UNABLE_TO_CREATE_MAPPER, clazz.getSimpleName() + MAPPER), e);
        } catch (NullPointerException e) {
            throw new MappingGeneratorException(format(UNABLE_TO_FIND_A_PATH_FOR_CLASS, clazz.getSimpleName()), e);
        } catch (Throwable e) {
            throw new MappingGeneratorException(format(UNABLE_TO_CREATE_MAPPER_UNKNOWN_ERROR, clazz.getSimpleName()), e);
        }
    }

    /**
     * Creating unit mapping class for both request and response model.
     *
     * @param request       - class of request model.
     * @param response      - class of response model.
     * @param genPackage    - string value of mapper's package.
     * @param jarPathToMain - classpath from root to main.
     * @throws MappingGeneratorException is thrown when IOException or NullPointerException
     *                                   happens while writing to file.
     */
    static void createRequestResponseMapperClass(Class<?> request, Class<?> response, String genPackage, String jarPathToMain)
            throws MappingGeneratorException {
        printMessage(format(START_GENERATING, request.getSimpleName().replace(REQUEST, EMPTY_STRING) + REQUEST + RESPONSE + MAPPER));
        for (int i = 0; i < request.getClasses().length; i++) {
            if (!request.getClasses()[i].getSimpleName().equals(request.getSimpleName() + BUILDER) &&
                    !generatedFiles.contains(request.getClasses()[i]) &&
                    !request.getClasses()[i].isAnnotationPresent(IgnoreGeneration.class) &&
                    !request.isEnum()) {
                createMapperClass(request.getClasses()[i], genPackage, jarPathToMain);
            }
        }
        for (int i = 0; i < response.getClasses().length; i++) {
            if (!response.getClasses()[i].getSimpleName().equals(response.getSimpleName() + BUILDER) &&
                    !generatedFiles.contains(response.getClasses()[i]) &&
                    !response.getClasses()[i].isAnnotationPresent(IgnoreGeneration.class) &&
                    !response.isEnum()) {
                createMapperClass(response.getClasses()[i], genPackage, jarPathToMain);
            }
        }

        String newClassName = request.getSimpleName().replace(REQUEST, EMPTY_STRING) + REQUEST + RESPONSE + MAPPER;
        TypeSpec mapper = interfaceBuilder(newClassName)
                .addModifiers(PUBLIC, STATIC)
                .build();
        TypeSpec requestMapperType = generateMappers(request, jarPathToMain);
        TypeSpec responseMapperType = generateMappers(response, jarPathToMain);

        mapper = mapper.toBuilder().addType(requestMapperType).addType(responseMapperType).build();
        JavaFile javaFile = JavaFile.builder(genPackage, mapper)
                .indent(TABULATION)
                .addStaticImport(CheckerForEmptiness.class, IS_NOT_EMPTY)
                .build();
        try {
            StringBuilder requestJarPath = new StringBuilder(request.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath());

            if (requestJarPath.toString().contains(DOT_JAR)) {
                String[] pathParts = requestJarPath.toString().split(separator);
                String temp = jarPathToMain.substring(0, jarPathToMain.substring(0, jarPathToMain.lastIndexOf(BUILD)).lastIndexOf(separator));
                requestJarPath.replace(0, requestJarPath.length(), temp.substring(0, temp.lastIndexOf(separator)))
                        .append(separator)
                        .append(BUILD)
                        .append(separator);
            }

            javaFile.writeTo(new File(requestJarPath.subSequence(0, requestJarPath.indexOf(BUILD)).toString() + SRC_MAIN_JAVA));
            generatedFiles.add(request);
            generatedFiles.add(response);
            printMessage(format(GENERATED_SUCCESSFULLY, request.getSimpleName().replace(REQUEST, EMPTY_STRING)
                    + REQUEST
                    + RESPONSE
                    + MAPPER));
        } catch (StringIndexOutOfBoundsException e) {
            throw new MappingGeneratorException(format(UNABLE_TO_PARSE_JAR_PATH, request.getSimpleName()), e);
        } catch (IOException e) {
            throw new MappingGeneratorException(format(UNABLE_TO_CREATE_REQ_RES_MAPPER, newClassName), e);
        } catch (NullPointerException e) {
            throw new MappingGeneratorException(format(UNABLE_TO_FIND_A_PATH_FOR_CLASS, request.getSimpleName()), e);
        } catch (Throwable e) {
            throw new MappingGeneratorException(format(UNABLE_TO_CREATE_MAPPER_UNKNOWN_ERROR, request.getSimpleName()), e);
        }
    }

    /**
     * Generate builder for ToModel block of ValueToModelMapper.
     * Example:
     * ValueToModelMapper<Model, Entity> toModel = entity -> Model.builder()
     * .build();
     *
     * @param clazz         - class of model for mappers generating.
     * @param jarPathToMain - classpath from root to main.
     * @return FieldSpec containing ToModel block.
     */
    static FieldSpec generateToModelBlock(Class<?> clazz, String jarPathToMain) {
        List<CodeBlock> codeBlocks = dynamicArrayOf(of(ENTITY_TO_MODEL_LAMBDA, clazz));
        List<String> notGeneratedFields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            switch (field.getType().getName()) {
                case CLASS_STRING:
                    codeBlocks.add(getBuilderLineForField(GET_ENTITY_STRING, field.getName(), field.getName()));
                    break;
                case CLASS_INTEGER:
                case CLASS_INTEGER_UNBOX:
                    codeBlocks.add(getBuilderLineForField(GET_ENTITY_INT, field.getName(), field.getName()));
                    break;
                case CLASS_DOUBLE:
                case CLASS_DOUBLE_UNBOX:
                    codeBlocks.add(getBuilderLineForField(GET_ENTITY_DOUBLE, field.getName(), field.getName()));
                    break;
                case CLASS_LONG:
                case CLASS_LONG_UNBOX:
                    codeBlocks.add(getBuilderLineForField(GET_ENTITY_LONG, field.getName(), field.getName()));
                    break;
                case CLASS_BYTE:
                case CLASS_BYTE_UNBOX:
                    codeBlocks.add(getBuilderLineForField(GET_ENTITY_BYTE, field.getName(), field.getName()));
                    break;
                case CLASS_BOOLEAN:
                case CLASS_BOOLEAN_UNBOX:
                    codeBlocks.add(getBuilderLineForField(GET_ENTITY_BOOL, field.getName(), field.getName()));
                    break;
                case CLASS_FLOAT:
                case CLASS_FLOAT_UNBOX:
                    codeBlocks.add(getBuilderLineForField(GET_ENTITY_FLOAT, field.getName(), field.getName()));
                    break;
                case CLASS_DATE:
                    codeBlocks.add(getBuilderLineForField(GET_ENTITY_DATE, field.getName(), field.getName()));
                    break;
                case CLASS_LIST:
                    try {
                        codeBlocks.add(generateToModelForList(field, jarPathToMain));
                    } catch (DefinitionException | InnerClassGenerationException e) {
                        notGeneratedFields.add(field.getName());
                        printError(e.getMessage());
                    }
                    break;
                case CLASS_SET:
                    try {
                        codeBlocks.add(generateToModelForSet(field, jarPathToMain));
                    } catch (DefinitionException | InnerClassGenerationException e) {
                        notGeneratedFields.add(field.getName());
                        printError(e.getCause() + SPACE + e.getMessage());
                    }
                    break;
                case CLASS_MAP:
                    try {
                        codeBlocks.add(generateToModelForMap(field, jarPathToMain));
                    } catch (DefinitionException | InnerClassGenerationException e) {
                        notGeneratedFields.add(field.getName());
                        printError(e.getCause() + SPACE + e.getMessage());
                    }
                    break;
                case CLASS_QUEUE:
                    try {
                        codeBlocks.add(generateToModelForQueue(field, jarPathToMain));
                    } catch (DefinitionException | InnerClassGenerationException e) {
                        notGeneratedFields.add(field.getName());
                        printError(e.getCause() + SPACE + e.getMessage());
                    }
                    break;
                default:
                    if (field.getType().getName().equals(clazz.getName()))
                        try {
                            codeBlocks.add(of(DOUBLE_TABULATION + GET_ENUM_VALUE, field.getName(), clazz, field.getName()));
                        } catch (DefinitionException | InnerClassGenerationException e) {
                            notGeneratedFields.add(field.getName());
                            printError(e.getCause() + SPACE + e.getMessage());
                        }
                    else
                        try {
                            codeBlocks.add(generateFromEntityFieldMapperCodeBlock(field.getType(), field.getName(), jarPathToMain));
                        } catch (DefinitionException | InnerClassGenerationException e) {
                            notGeneratedFields.add(field.getName());
                            printError(e.getCause() + SPACE + e.getMessage());
                        }

            }
        }
        codeBlocks.add(of(DOUBLE_TABULATION + DEFAULT_ENTITY_BUILDER, clazz));
        if (notGeneratedFields.isEmpty())
            return FieldSpec.builder(ParameterizedTypeName.get(ValueToModelMapper.class, clazz, Entity.class), TO_MODEL + clazz.getSimpleName(), PUBLIC, STATIC, FINAL)
                    .initializer(join(codeBlocks, NEW_LINE))
                    .build();
        else
            return FieldSpec.builder(ParameterizedTypeName.get(ValueToModelMapper.class, clazz, Entity.class), TO_MODEL + clazz.getSimpleName(), PUBLIC, STATIC, FINAL)
                    .initializer(join(codeBlocks, NEW_LINE))
                    .addAnnotation(AnnotationSpec.builder(GenerationException.class).addMember(NOT_GENERATED_FIELDS, STRING_PATTERN, notGeneratedFields).build())
                    .build();
    }

    /**
     * Generate builder for FromModel block of ValueFromModelMapper.
     * Example:
     * ValueFromModelMapper<Model, Entity> fromModel = model -> Entity.entityBuilder()
     * .build();
     *
     * @param clazz         - class of model for mappers generating.
     * @param jarPathToMain - classpath from root to main.
     * @return FieldSpec containing FromModel block.
     */
    static FieldSpec generateFromModelBlock(Class<?> clazz, String jarPathToMain) {
        List<CodeBlock> codeBlocks = dynamicArrayOf(of(MODEL_TO_ENTITY_LAMBDA, Entity.class));
        List<String> notGeneratedFields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            String getField;
            if (field.getType().getName().equals(CLASS_BOOLEAN_UNBOX))
                getField = IS + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);
            else
                getField = GET + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);

            switch (field.getType().getName()) {
                case CLASS_STRING:
                    codeBlocks.add(getBuilderLineForField(STRING_FIELD, field.getName(), getField));
                    break;
                case CLASS_INTEGER:
                case CLASS_INTEGER_UNBOX:
                    codeBlocks.add(getBuilderLineForField(INT_FIELD, field.getName(), getField));
                    break;
                case CLASS_DOUBLE:
                case CLASS_DOUBLE_UNBOX:
                    codeBlocks.add(getBuilderLineForField(DOUBLE_FIELD, field.getName(), getField));
                    break;
                case CLASS_LONG:
                case CLASS_LONG_UNBOX:
                    codeBlocks.add(getBuilderLineForField(LONG_FIELD, field.getName(), getField));
                    break;
                case CLASS_BYTE:
                case CLASS_BYTE_UNBOX:
                    codeBlocks.add(getBuilderLineForField(BYTE_FIELD, field.getName(), getField));
                    break;
                case CLASS_BOOLEAN:
                case CLASS_BOOLEAN_UNBOX:
                    codeBlocks.add(getBuilderLineForField(BOOL_FIELD, field.getName(), getField));
                    break;
                case CLASS_FLOAT:
                case CLASS_FLOAT_UNBOX:
                    codeBlocks.add(getBuilderLineForField(FLOAT_FIELD, field.getName(), getField));
                    break;
                case CLASS_DATE:
                    codeBlocks.add(getBuilderLineForField(DATE_FIELD, field.getName(), getField));
                    break;
                case CLASS_LIST:
                case CLASS_SET:
                    try {
                        codeBlocks.add(generateFromModelForCollection(field, jarPathToMain));
                    } catch (DefinitionException | InnerClassGenerationException e) {
                        notGeneratedFields.add(field.getName());
                        printError(e.getCause() + SPACE + e.getMessage());
                    }
                    break;
                case CLASS_MAP:
                    try {
                        codeBlocks.add(generateFromModelForMap(field, jarPathToMain));
                    } catch (DefinitionException | InnerClassGenerationException e) {
                        notGeneratedFields.add(field.getName());
                        printError(e.getCause() + SPACE + e.getMessage());
                    }
                    break;
                case CLASS_QUEUE:
                    try {
                        codeBlocks.add(generateFromModelForCollection(field, jarPathToMain));
                    } catch (DefinitionException | InnerClassGenerationException e) {
                        notGeneratedFields.add(field.getName());
                        printError(e.getCause() + SPACE + e.getMessage());
                    }
                    break;
                default:
                    if (field.getType().getName().equals(clazz.getName()))
                        try {
                            codeBlocks.add(of(DOUBLE_TABULATION + ENUM_FILED, field.getName(), CheckerForEmptiness.class, EMPTY_IF_NULL, getField));
                        } catch (DefinitionException | InnerClassGenerationException e) {
                            notGeneratedFields.add(field.getName());
                            printError(e.getCause() + SPACE + e.getMessage());
                        }
                    else
                        try {
                            codeBlocks.add(generateToEntityFieldMapperCodeBlock(field.getType(), field.getName(), getField, jarPathToMain));
                        } catch (DefinitionException | InnerClassGenerationException e) {
                            notGeneratedFields.add(field.getName());
                            printError(e.getCause() + SPACE + e.getMessage());
                        }
            }
        }

        codeBlocks.add(of(DOUBLE_TABULATION + DEFAULT_MODEL_BUILDER, Entity.class));
        if (notGeneratedFields.isEmpty())
            return FieldSpec.builder(ParameterizedTypeName.get(ValueFromModelMapper.class, clazz, Entity.class), FROM_MODEL + clazz.getSimpleName(), PUBLIC, STATIC, FINAL)
                    .initializer(join(codeBlocks, NEW_LINE))
                    .build();
        else
            return FieldSpec.builder(ParameterizedTypeName.get(ValueFromModelMapper.class, clazz, Entity.class), FROM_MODEL + clazz.getSimpleName(), PUBLIC, STATIC, FINAL)
                    .initializer(join(codeBlocks, NEW_LINE))
                    .addAnnotation(AnnotationSpec.builder(GenerationException.class).addMember(NOT_GENERATED_FIELDS, STRING_PATTERN, notGeneratedFields).build())
                    .build();
    }

    /**
     * Create new ordinary mapper class for processing field and
     * generate codeblock for "from entity to model" string in ToModel builder.
     *
     * @param clazz         - processing field type.
     * @param fieldName     - name of processing field.
     * @param jarPathToMain - classpath from root to main.
     * @return CodeBlock for entity of ToModel mapper.
     * @throws MappingGeneratorException can be thrown by createMapperClass.
     */
    static CodeBlock generateFromEntityFieldMapperCodeBlock(Class<?> clazz, String fieldName, String jarPathToMain)
            throws InnerClassGenerationException {
        ClassName className = createMapperForInnerClassIfNeeded(clazz, jarPathToMain);
        if (clazz.getName().equals(className.packageName()) && clazz.getSimpleName().equals(className.simpleName()))
            return CodeBlock.builder()
                    .add(of(DOUBLE_TABULATION + GET_ENUM_VALUE, fieldName, clazz, fieldName))
                    .build();
        return CodeBlock.builder()
                .add(of(DOUBLE_TABULATION + GET_VALUE, fieldName, fieldName, className, TO_MODEL + clazz.getSimpleName()))
                .build();
    }

    /**
     * Create new ordinary mapper class for processing field and
     * generate codeblock for "to entity from model" string in FromModel builder.
     *
     * @param clazz         - processing field type.
     * @param fieldName     - name of processing field.
     * @param getField      - string representing "get" + name of processing field.
     * @param jarPathToMain - classpath from root to main.
     * @return CodeBlock for entity of FromModel mapper.
     * @throws MappingGeneratorException can be thrown by createMapperClass.
     */
    static CodeBlock generateToEntityFieldMapperCodeBlock(Class<?> clazz, String fieldName, String getField, String jarPathToMain)
            throws InnerClassGenerationException {
        ClassName className = createMapperForInnerClassIfNeeded(clazz, jarPathToMain);
        if (clazz.getName().equals(className.packageName()) && clazz.getSimpleName().equals(className.simpleName()))
            return CodeBlock.builder()
                    .add(of(DOUBLE_TABULATION + ENUM_FILED, fieldName, CheckerForEmptiness.class, EMPTY_IF_NULL, getField))
                    .build();
        return CodeBlock.builder()
                .add(of(DOUBLE_TABULATION + ENTITY_FIELD, fieldName, getField, className, FROM_MODEL + clazz.getSimpleName()))
                .build();
    }

    /**
     * Returning codeblock of generating string
     *
     * @param pattern        - pattern of string in builder.
     *                       Example: ".stringField($S, model.$L())"
     * @param fieldName      - name of processing field.
     * @param fieldNameToGet - either name of processing field or "get" + name of processing field.
     * @return CodeBlock of generating string.
     */
    static CodeBlock getBuilderLineForField(String pattern, String fieldName, String fieldNameToGet) {
        return of(DOUBLE_TABULATION + pattern, fieldName, fieldNameToGet);
    }

    /**
     * Method generating block of fields' names constants for each mapper.
     *
     * @param clazz - class of model for mappers generating.
     * @return fields' names as string constants.
     */
    static Iterable<FieldSpec> generateConstantsBlock(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .map(declaredFields -> FieldSpec.builder(String.class, declaredFields.getName())
                        .addModifiers(PUBLIC, STATIC, FINAL)
                        .initializer(STRING_PATTERN, declaredFields.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
