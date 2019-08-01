package ru.art.generator.mapper.operations;

import com.squareup.javapoet.ClassName;
import ru.art.generator.mapper.annotation.NonGenerated;
import ru.art.generator.mapper.exception.InnerClassGenerationException;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.StringConstants.DOT;
import static ru.art.generator.mapper.constants.Constants.MAPPER;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.MAPPING;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.MODEL;
import static ru.art.generator.mapper.constants.ExceptionConstants.MapperGeneratorExceptions.UNABLE_TO_CREATE_INNER_CLASS_MAPPER;
import static ru.art.generator.mapper.operations.GeneratorOperations.createMapperClass;
import static ru.art.generator.mapper.operations.GeneratorOperations.generatedFiles;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

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
    static ClassName createMapperForInnerClassIfNeeded(Class genClass, String jarPathToMain) {
        if (genClass.isEnum()) return ClassName.get(genClass);
        if (!genClass.isAnnotationPresent(NonGenerated.class)) {
            String genPackage = genClass.getPackage().getName().contains(MODEL) ?
                    genClass.getPackage().getName().replace(MODEL, MAPPING) :
                    genClass.getPackage().getName().substring(0, genClass.getPackage().getName().lastIndexOf(DOT)) + DOT + MAPPING;
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
        Class genClass = (Class) type.getActualTypeArguments()[0];
        if (genClass.isEnum()) return ClassName.get(genClass);
        if (!genClass.isAnnotationPresent(NonGenerated.class)) {
            String genPackage = genClass.getPackage().getName().contains(MODEL) ?
                    genClass.getPackage().getName().replace(MODEL, MAPPING) :
                    genClass.getPackage().getName().substring(0, genClass.getPackage().getName().lastIndexOf(DOT)) + DOT + MAPPING;
            if (!generatedFiles.contains(genClass))
                createMapperClass(genClass, genPackage, jarPathToMain);
            return ClassName.get(genPackage, genClass.getSimpleName() + MAPPER);
        }
        throw new InnerClassGenerationException(format(UNABLE_TO_CREATE_INNER_CLASS_MAPPER, genClass));
    }
}
