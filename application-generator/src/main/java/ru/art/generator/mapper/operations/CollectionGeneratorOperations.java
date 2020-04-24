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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import ru.art.entity.PrimitiveMapping;
import ru.art.generator.mapper.exception.DefinitionException;
import ru.art.generator.mapper.exception.InnerClassGenerationException;
import ru.art.generator.mapper.models.GenerationPackageModel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.squareup.javapoet.CodeBlock.of;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.mapper.constants.Constants.GET;
import static ru.art.generator.mapper.constants.Constants.MAPPER;
import static ru.art.generator.mapper.constants.Constants.SupportedJavaClasses.*;
import static ru.art.generator.mapper.constants.Constants.SymbolsAndFormatting.PATTERN_FOR_GENERIC_INNER_TYPES;
import static ru.art.generator.mapper.constants.ExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_GENERIC_TYPE;
import static ru.art.generator.mapper.constants.FromModelConstants.*;
import static ru.art.generator.mapper.constants.ToModelConstants.*;
import static ru.art.generator.mapper.operations.CommonOperations.createMapperForInnerClassIfNeeded;
import static ru.art.generator.mapper.operations.CommonOperations.getClassFromField;

/**
 * Class containing static methods for generating
 * strings in builder for collections.
 */
public final class CollectionGeneratorOperations {
    private CollectionGeneratorOperations() {
    }

    /**
     * Generate block code for List type for to model mapper.
     *
     * @param field         - class' field with List type.
     * @param generationInfo - information about packages and path for generated class.
     * @return CodeBlock for List field.
     * @throws DefinitionException is thrown when field's generic type isn't supported.
     */
    public static CodeBlock generateToModelForList(Field field, GenerationPackageModel generationInfo) throws DefinitionException, InnerClassGenerationException {
        if (field.getGenericType().getTypeName().equals(CLASS_LIST) || field.getGenericType().getTypeName().matches(PATTERN_FOR_GENERIC_INNER_TYPES))
            throw new DefinitionException(format(UNABLE_TO_DEFINE_GENERIC_TYPE,
                    field.getGenericType().getTypeName(),
                    field.getName(),
                    field.getDeclaringClass().getSimpleName(),
                    TO_MODEL + field.getDeclaringClass().getSimpleName()));
        if (field.getGenericType().getTypeName().contains(CLASS_STRING))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_STRING_LIST, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_INTEGER))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_INT_LIST, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_DOUBLE))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_DOUBLE_LIST, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_LONG))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_LONG_LIST, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_BYTE))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_BYTE_LIST, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_BOOLEAN))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_BOOL_LIST, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_FLOAT))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_FLOAT_LIST, field.getName(), field.getName())).build();

        ClassName newClassMapper = createMapperForInnerClassIfNeeded(getClassFromField(field), generationInfo);
        return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_ENTITY_LIST,
                field.getName(),
                field.getName(),
                newClassMapper,
                TO_MODEL + newClassMapper.simpleName().replace(MAPPER, EMPTY_STRING))).build();
    }

    /**
     * Generate block code for Set type for to model mapper.
     *
     * @param field         - class' field with Set type.
     * @param generationInfo - information about packages and path for generated class.
     * @return CodeBlock for Set field.
     * @throws DefinitionException is thrown when field's generic type isn't supported.
     */
    public static CodeBlock generateToModelForSet(Field field, GenerationPackageModel generationInfo) throws DefinitionException, InnerClassGenerationException {
        if (field.getGenericType().getTypeName().equals(CLASS_SET) || field.getGenericType().getTypeName().matches(PATTERN_FOR_GENERIC_INNER_TYPES))
            throw new DefinitionException(format(UNABLE_TO_DEFINE_GENERIC_TYPE,
                    field.getGenericType().getTypeName(),
                    field.getName(),
                    field.getDeclaringClass().getSimpleName(),
                    TO_MODEL + field.getDeclaringClass().getSimpleName()));
        if (field.getGenericType().getTypeName().contains(CLASS_STRING))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_STRING_SET, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_INTEGER))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_INT_SET, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_DOUBLE))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_DOUBLE_SET, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_LONG))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_LONG_SET, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_BYTE))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_BYTE_SET, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_BOOLEAN))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_BOOL_SET, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_FLOAT))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_FLOAT_SET, field.getName(), field.getName())).build();

        ClassName newClassMapper = createMapperForInnerClassIfNeeded(getClassFromField(field), generationInfo);
        return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_ENTITY_SET,
                field.getName(),
                field.getName(),
                newClassMapper,
                TO_MODEL + newClassMapper.simpleName().replace(MAPPER, EMPTY_STRING))).build();
    }

    /**
     * Generate block code for Queue type for to model mapper.
     *
     * @param field         - class' field with Queue type.
     * @param generationInfo - information about packages and path for generated class.
     * @return CodeBlock for Queue field.
     * @throws DefinitionException is thrown when field's generic type isn't supported.
     */
    public static CodeBlock generateToModelForQueue(Field field, GenerationPackageModel generationInfo) throws DefinitionException, InnerClassGenerationException {
        if (field.getGenericType().getTypeName().equals(CLASS_QUEUE) || field.getGenericType().getTypeName().matches(PATTERN_FOR_GENERIC_INNER_TYPES))
            throw new DefinitionException(format(UNABLE_TO_DEFINE_GENERIC_TYPE,
                    field.getGenericType().getTypeName(),
                    field.getName(),
                    field.getDeclaringClass().getSimpleName(),
                    TO_MODEL + field.getDeclaringClass().getSimpleName()));
        if (field.getGenericType().getTypeName().contains(CLASS_STRING))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_STRING_QUEUE, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_INTEGER))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_INT_QUEUE, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_DOUBLE))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_DOUBLE_QUEUE, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_LONG))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_LONG_QUEUE, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_BYTE))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_BYTE_QUEUE, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_BOOLEAN))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_BOOL_QUEUE, field.getName(), field.getName())).build();
        if (field.getGenericType().getTypeName().contains(CLASS_FLOAT))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_FLOAT_QUEUE, field.getName(), field.getName())).build();

        ClassName newClassMapper = createMapperForInnerClassIfNeeded(getClassFromField(field), generationInfo);
        return CodeBlock.builder().add(of(DOUBLE_TABULATION + GET_ENTITY_QUEUE,
                field.getName(),
                field.getName(),
                newClassMapper,
                TO_MODEL + newClassMapper.simpleName().replace(MAPPER, EMPTY_STRING))).build();
    }

    /**
     * Generate block code for Map type for to model mapper.
     *
     * @param field         - class' field with Map type.
     * @param generationInfo - information about packages and path for generated class.
     * @return CodeBlock for Map field.
     * @throws DefinitionException is thrown when field's generic type isn't supported.
     */
    public static CodeBlock generateToModelForMap(Field field, GenerationPackageModel generationInfo) throws DefinitionException, InnerClassGenerationException {
        if (field.getGenericType().getTypeName().equals(CLASS_MAP) || field.getGenericType().getTypeName().matches(PATTERN_FOR_GENERIC_INNER_TYPES))
            throw new DefinitionException(format(UNABLE_TO_DEFINE_GENERIC_TYPE,
                    field.getGenericType().getTypeName(),
                    field.getName(),
                    field.getDeclaringClass().getSimpleName(),
                    TO_MODEL + field.getDeclaringClass().getSimpleName()));
        Type genericType = field.getGenericType();
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type keyType = parameterizedType.getActualTypeArguments()[0];
        Type valueType = parameterizedType.getActualTypeArguments()[1];
        String mapGetter = calculateMapGetter(keyType.getTypeName(), valueType.getTypeName());

        Class<?> keyClass = (Class<?>) keyType;
        Class<?> valueClass = (Class<?>) valueType;

        switch (mapGetter) {
            case GET_MAP_PRIMITIVE_KEY:
                createMapperForInnerClassIfNeeded(valueClass, generationInfo);
                return CodeBlock.builder()
                        .add(of(DOUBLE_TABULATION + mapGetter,
                                field.getName(),
                                field.getName(),
                                PrimitiveMapping.class,
                                getToModelMappingFromType(keyType),
                                getToModelMappingFromType(valueType)))
                        .build();
            case GET_MAP_PRIMITIVE_VALUE:
                createMapperForInnerClassIfNeeded(keyClass, generationInfo);
                return CodeBlock.builder()
                        .add(of(DOUBLE_TABULATION + mapGetter,
                                field.getName(),
                                field.getName(),
                                getToModelMappingFromType(keyType),
                                PrimitiveMapping.class,
                                getToModelMappingFromType(valueType)))
                        .build();
            case GET_MAP_PRIMITIVE_KEY_VALUE:
                return CodeBlock.builder()
                        .add(of(DOUBLE_TABULATION + mapGetter,
                                field.getName(),
                                field.getName(),
                                PrimitiveMapping.class,
                                getToModelMappingFromType(keyType),
                                PrimitiveMapping.class,
                                getToModelMappingFromType(valueType)))
                        .build();
            default:
                createMapperForInnerClassIfNeeded(valueClass, generationInfo);
                createMapperForInnerClassIfNeeded(keyClass, generationInfo);
                return CodeBlock.builder()
                        .add(of(DOUBLE_TABULATION + mapGetter,
                                field.getName(),
                                field.getName(),
                                getToModelMappingFromType(keyType),
                                getToModelMappingFromType(valueType)))
                        .build();
        }
    }

    /**
     * Generate block code for collection for from model mapper.
     * Supported collections: List, Set, Queue.
     *
     * @param field         - class' field with one of supported types.
     * @param generationInfo - information about packages and path for generated class.
     * @return CodeBlock for field of one of supported types.
     * @throws DefinitionException is thrown when field's generic type isn't supported.
     */
    public static CodeBlock generateFromModelForCollection(Field field, GenerationPackageModel generationInfo) throws DefinitionException, InnerClassGenerationException {
        String typeName = field.getGenericType().getTypeName();

        if (typeName.matches(PATTERN_FOR_GENERIC_INNER_TYPES) ||
                typeName.equals(CLASS_LIST) ||
                typeName.equals(CLASS_SET) ||
                typeName.equals(CLASS_QUEUE))
            throw new DefinitionException(format(UNABLE_TO_DEFINE_GENERIC_TYPE,
                    typeName,
                    field.getName(),
                    field.getDeclaringClass().getSimpleName(),
                    FROM_MODEL + field.getDeclaringClass().getSimpleName()));

        String getField = GET + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);

        if (typeName.contains(CLASS_STRING))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + STRING_COLLECTION_FIELD, field.getName(), getField)).build();
        if (typeName.contains(CLASS_INTEGER))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + INT_COLLECTION_FIELD, field.getName(), getField)).build();
        if (typeName.contains(CLASS_DOUBLE))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + DOUBLE_COLLECTION_FIELD, field.getName(), getField)).build();
        if (typeName.contains(CLASS_LONG))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + LONG_COLLECTION_FIELD, field.getName(), getField)).build();
        if (typeName.contains(CLASS_BYTE))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + BYTE_COLLECTION_FIELD, field.getName(), getField)).build();
        if (typeName.contains(CLASS_BOOLEAN))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + BOOL_COLLECTION_FIELD, field.getName(), getField)).build();
        if (typeName.contains(CLASS_FLOAT))
            return CodeBlock.builder().add(of(DOUBLE_TABULATION + FLOAT_COLLECTION_FIELD, field.getName(), getField)).build();

        ClassName newClassMapper = createMapperForInnerClassIfNeeded(getClassFromField(field), generationInfo);
        return CodeBlock.builder().add(of(DOUBLE_TABULATION + ENTITY_COLLECTION_FIELD,
                field.getName(),
                getField,
                newClassMapper,
                FROM_MODEL + newClassMapper.simpleName().replace(MAPPER, EMPTY_STRING))).build();
    }

    /**
     * Generate block code for Map type for from model mapper.
     *
     * @param field         - class' field with Map type.
     * @param generationInfo - information about packages and path for generated class.
     * @return CodeBlock for Map field.
     * @throws DefinitionException is thrown when field's generic type isn't supported.
     */
    public static CodeBlock generateFromModelForMap(Field field, GenerationPackageModel generationInfo) throws DefinitionException, InnerClassGenerationException {
        if (field.getGenericType().getTypeName().equals(CLASS_MAP) || field.getGenericType().getTypeName().matches(PATTERN_FOR_GENERIC_INNER_TYPES))
            throw new DefinitionException(format(UNABLE_TO_DEFINE_GENERIC_TYPE,
                    field.getGenericType().getTypeName(),
                    field.getName(),
                    field.getDeclaringClass().getSimpleName(),
                    FROM_MODEL + field.getDeclaringClass().getSimpleName()));
        String getField = GET + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);
        Type genericType = field.getGenericType();
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type keyType = parameterizedType.getActualTypeArguments()[0];
        Type valueType = parameterizedType.getActualTypeArguments()[1];
        String mapBuilder = calculateMapBuilder(keyType.getTypeName(), valueType.getTypeName());

        Class keyClass = (Class) keyType;
        Class valueClass = (Class) valueType;

        switch (mapBuilder) {
            case MAP_FIELD_PRIMITIVE_KEY:
                createMapperForInnerClassIfNeeded(valueClass, generationInfo);
                return CodeBlock.builder()
                        .add(of(DOUBLE_TABULATION + mapBuilder,
                                field.getName(),
                                getField,
                                PrimitiveMapping.class,
                                getFromModelMappingFromType(keyType),
                                getFromModelMappingFromType(valueType)))
                        .build();
            case MAP_FIELD_PRIMITIVE_VALUE:
                createMapperForInnerClassIfNeeded(keyClass, generationInfo);
                return CodeBlock.builder()
                        .add(of(DOUBLE_TABULATION + mapBuilder,
                                field.getName(),
                                getField,
                                getFromModelMappingFromType(keyType),
                                PrimitiveMapping.class,
                                getFromModelMappingFromType(valueType)))
                        .build();
            case MAP_FIELD_PRIMITIVE_KEY_VALUE:
                return CodeBlock.builder()
                        .add(of(DOUBLE_TABULATION + mapBuilder,
                                field.getName(),
                                getField,
                                PrimitiveMapping.class,
                                getFromModelMappingFromType(keyType),
                                PrimitiveMapping.class,
                                getFromModelMappingFromType(valueType)))
                        .build();
            default:
                createMapperForInnerClassIfNeeded(valueClass, generationInfo);
                createMapperForInnerClassIfNeeded(keyClass, generationInfo);
                return CodeBlock.builder()
                        .add(of(DOUBLE_TABULATION + mapBuilder,
                                field.getName(),
                                getField,
                                getFromModelMappingFromType(keyType),
                                getFromModelMappingFromType(valueType)))
                        .build();
        }
    }

    /**
     * Define which pattern should be taken for ToModelForMap mapper.
     *
     * @param keyType   - type of map's key.
     * @param valueType - type of map's value.
     * @return pattern.
     */
    public static String calculateMapGetter(String keyType, String valueType) {
        String getter;
        switch (keyType) {
            case CLASS_STRING:
            case CLASS_INTEGER:
            case CLASS_DOUBLE:
            case CLASS_LONG:
            case CLASS_BYTE:
            case CLASS_BOOLEAN:
            case CLASS_FLOAT:
                getter = GET_MAP_PRIMITIVE_KEY;
                break;
            default:
                getter = GET_MAP;
        }
        switch (valueType) {
            case CLASS_STRING:
            case CLASS_INTEGER:
            case CLASS_DOUBLE:
            case CLASS_LONG:
            case CLASS_BYTE:
            case CLASS_BOOLEAN:
            case CLASS_FLOAT:
                return getter.equals(GET_MAP_PRIMITIVE_KEY) ? GET_MAP_PRIMITIVE_KEY_VALUE : GET_MAP_PRIMITIVE_VALUE;
            default:
                return getter.equals(GET_MAP_PRIMITIVE_KEY) ? GET_MAP_PRIMITIVE_KEY : GET_MAP;
        }
    }

    /**
     * Define which pattern should be taken for FromModelForMap mapper.
     *
     * @param keyType   - type of map's key.
     * @param valueType - type of map's value.
     * @return pattern.
     */
    public static String calculateMapBuilder(String keyType, String valueType) {
        String builder;
        switch (keyType) {
            case CLASS_STRING:
            case CLASS_INTEGER:
            case CLASS_DOUBLE:
            case CLASS_LONG:
            case CLASS_BYTE:
            case CLASS_BOOLEAN:
            case CLASS_FLOAT:
                builder = MAP_FIELD_PRIMITIVE_KEY;
                break;
            default:
                builder = MAP_FIELD;
        }
        switch (valueType) {
            case CLASS_STRING:
            case CLASS_INTEGER:
            case CLASS_DOUBLE:
            case CLASS_LONG:
            case CLASS_BYTE:
            case CLASS_BOOLEAN:
            case CLASS_FLOAT:
                return builder.equals(MAP_FIELD_PRIMITIVE_KEY) ? MAP_FIELD_PRIMITIVE_KEY_VALUE : MAP_FIELD_PRIMITIVE_VALUE;
            default:
                return builder.equals(MAP_FIELD_PRIMITIVE_KEY) ? MAP_FIELD_PRIMITIVE_KEY : MAP_FIELD;
        }
    }

    /**
     * Check type of key or value of the map and return mapper
     * in string representation as parameter in ValueToModelMapper's entity.getValue.
     *
     * @param type - type of map's key or value.
     * @return string value of toModel parameter mapping.
     */
    public static String getToModelMappingFromType(Type type) {
        switch (type.getTypeName()) {
            case CLASS_STRING:
                return STRING_TO_MODEL;
            case CLASS_INTEGER:
                return INT_TO_MODEL;
            case CLASS_DOUBLE:
                return DOUBLE_TO_MODEL;
            case CLASS_LONG:
                return LONG_TO_MODEL;
            case CLASS_BYTE:
                return BYTE_TO_MODEL;
            case CLASS_BOOLEAN:
                return BOOL_TO_MODEL;
            case CLASS_FLOAT:
                return FLOAT_TO_MODEL;
        }
        String className = ((Class<?>) type).getSimpleName();
        return className + MAPPER + DOT + TO_MODEL + className;
    }

    /**
     * Check type of key or value of the map and return mapper
     * in string representation as parameter in ValueFromModelMapper's entityField.
     *
     * @param type - type of map's key or value.
     * @return string value of fromModel parameter.
     */
    public static String getFromModelMappingFromType(Type type) {
        switch (type.getTypeName()) {
            case CLASS_STRING:
                return STRING_FROM_MODEL;
            case CLASS_INTEGER:
                return INT_FROM_MODEL;
            case CLASS_DOUBLE:
                return DOUBLE_FROM_MODEL;
            case CLASS_LONG:
                return LONG_FROM_MODEL;
            case CLASS_BYTE:
                return BYTE_FROM_MODEL;
            case CLASS_BOOLEAN:
                return BOOL_FROM_MODEL;
            case CLASS_FLOAT:
                return FLOAT_FROM_MODEL;
        }
        String className = ((Class<?>) type).getSimpleName();
        return className + MAPPER + DOT + FROM_MODEL + className;
    }
}
