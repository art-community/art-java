package ru.art.generator.common.operations;


import com.squareup.javapoet.CodeBlock;
import ru.art.generator.exception.NotSupportedTypeForPrimitiveMapperException;
import ru.art.generator.spec.http.servicespec.exception.HttpServiceSpecGeneratorException;
import static com.squareup.javapoet.CodeBlock.of;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.StringConstants.DOUBLE_TABULATION;
import static ru.art.core.constants.StringConstants.SLASH;
import static ru.art.generator.common.constants.Constants.PathAndPackageConstants.*;
import static ru.art.generator.common.constants.Constants.PrimitiveMapperConstants.*;
import static ru.art.generator.common.constants.Constants.SupportedJavaClasses.*;
import static ru.art.generator.common.constants.ExceptionConstants.NOT_SUPPORTED_TYPE_FOR_PRIMITIVE_MAPPER;
import static ru.art.generator.common.constants.ExceptionConstants.UNABLE_TO_PARSE_JAR_PATH;
import java.lang.reflect.Type;

/**
 * Interface containing common static methods which can be used
 * in other operations for all generators.
 */
public interface CommonOperations {

    /**
     * Wrap of System.out.println.
     * @param message - text to print.
     */
    static void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Wrap of System.err.println.
     * @param errorText - error to print.
     */
    static void printError(String errorText) {
        System.err.println(errorText);
    }

    /**
     * Returning codeblock of generating string
     * @param pattern - pattern of string in builder.
     * Example: ".post($S)"
     * @param field - name of processing field.
     * @return CodeBlock of generating string.
     */
    static CodeBlock getBuilderLineForField(String pattern, Object... field) {
        return of(DOUBLE_TABULATION + pattern, field);
    }

    /**
     * Form jar path to generated file.
     * @param modelClass - model class/interface.
     * @param jarPathToMain - classpath from root to main.
     * @return jar path of certain class.
     */
    static String defineClassJarPath(Class<?> modelClass, String jarPathToMain) {
        StringBuilder classJarPath = new StringBuilder(modelClass.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath());
        if (classJarPath.toString().contains(DOT_JAR)) {
            String[] pathParts = classJarPath.toString().split(SLASH);
            int moduleNameIndex = -1;
            for (int i = 0; i < pathParts.length; i++)
                if (RU_RTI_CRM.equals(pathParts[i])) {
                    moduleNameIndex = i + 1;
                    break;
                }
            if (moduleNameIndex == -1)
                throw new HttpServiceSpecGeneratorException(format(UNABLE_TO_PARSE_JAR_PATH, modelClass.getSimpleName()));
            String temp = jarPathToMain.substring(0, jarPathToMain.substring(0, jarPathToMain.lastIndexOf(BUILD)).lastIndexOf(SLASH));
            classJarPath.replace(0, classJarPath.length(), temp.substring(0, temp.lastIndexOf(SLASH)))
                    .append(pathParts[moduleNameIndex])
                    .append(SLASH)
                    .append(BUILD)
                    .append(SLASH);
        }
        return classJarPath.toString();
    }

    /**
     * Check type and return mapper in string representation
     * as parameter in ValueToModelMapper's entity.getValue.
     *
     * @param type - type to check.
     * @return string value of toModel parameter mapping.
     * @throws NotSupportedTypeForPrimitiveMapperException is thrown when there is an attempt to get primitive mapper
     *                                                     for not primitive type or not supported primitive type
     */
    static String getToModelMappingFromType(Type type) throws NotSupportedTypeForPrimitiveMapperException {
        switch (type.getTypeName()) {
            case CLASS_STRING:
                return STRING_TO_MODEL;
            case CLASS_INTEGER:
            case CLASS_INTEGER_UNBOX:
                return INT_TO_MODEL;
            case CLASS_DOUBLE:
            case CLASS_DOUBLE_UNBOX:
                return DOUBLE_TO_MODEL;
            case CLASS_LONG:
            case CLASS_LONG_UNBOX:
                return LONG_TO_MODEL;
            case CLASS_BYTE:
            case CLASS_BYTE_UNBOX:
                return BYTE_TO_MODEL;
            case CLASS_BOOLEAN:
            case CLASS_BOOLEAN_UNBOX:
                return BOOL_TO_MODEL;
            case CLASS_FLOAT:
            case CLASS_FLOAT_UNBOX:
                return FLOAT_TO_MODEL;
        }
        throw new NotSupportedTypeForPrimitiveMapperException(format(NOT_SUPPORTED_TYPE_FOR_PRIMITIVE_MAPPER, type.getTypeName()));
    }

    /**
     * Check type and return mapper in string representation
     * as parameter in ValueFromModelMapper's entityField.
     *
     * @param type - type to check.
     * @return string value of fromModel parameter.
     * @throws NotSupportedTypeForPrimitiveMapperException is thrown when there is an attempt to get primitive mapper
     *                                                     for not primitive type or not supported primitive type
     */
    static String getFromModelMappingFromType(Type type) throws NotSupportedTypeForPrimitiveMapperException {
        switch (type.getTypeName()) {
            case CLASS_STRING:
                return STRING_FROM_MODEL;
            case CLASS_INTEGER:
            case CLASS_INTEGER_UNBOX:
                return INT_FROM_MODEL;
            case CLASS_DOUBLE:
            case CLASS_DOUBLE_UNBOX:
                return DOUBLE_FROM_MODEL;
            case CLASS_LONG:
            case CLASS_LONG_UNBOX:
                return LONG_FROM_MODEL;
            case CLASS_BYTE:
            case CLASS_BYTE_UNBOX:
                return BYTE_FROM_MODEL;
            case CLASS_BOOLEAN:
            case CLASS_BOOLEAN_UNBOX:
                return BOOL_FROM_MODEL;
            case CLASS_FLOAT:
            case CLASS_FLOAT_UNBOX:
                return FLOAT_FROM_MODEL;
        }
        throw new NotSupportedTypeForPrimitiveMapperException(format(NOT_SUPPORTED_TYPE_FOR_PRIMITIVE_MAPPER, type.getTypeName()));
    }

    /**
     * Method checks if certain type is primitive or not.
     *
     * @param type - type to check.
     * @return - true: if class is one of supported primitives.
     * - false: if class isn't one of supported primitives.
     */
    static boolean isClassPrimitive(Type type) {
        switch (type.getTypeName()) {
            case CLASS_STRING:
            case CLASS_INTEGER:
            case CLASS_INTEGER_UNBOX:
            case CLASS_DOUBLE:
            case CLASS_DOUBLE_UNBOX:
            case CLASS_LONG:
            case CLASS_LONG_UNBOX:
            case CLASS_BYTE:
            case CLASS_BYTE_UNBOX:
            case CLASS_BOOLEAN:
            case CLASS_BOOLEAN_UNBOX:
            case CLASS_FLOAT:
            case CLASS_FLOAT_UNBOX:
                return true;
        }
        return false;
    }
}
