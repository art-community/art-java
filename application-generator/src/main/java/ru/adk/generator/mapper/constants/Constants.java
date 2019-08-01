package ru.adk.generator.mapper.constants;

/**
 * Interface for common constants of mapper generator
 */
public interface Constants {

    String BUILD_METHOD = ".build()";
    String MAPPER = "Mapper";
    String GET = "get";
    String IS = "is";
    String REQUEST = "Request";
    String RESPONSE = "Response";
    String START_GENERATING = "Start generating ''{0}''";
    String GENERATED_SUCCESSFULLY = "Mapper ''{0}'' has been generated \033[36msuccessfully\033[0m";
    String EMPTY_IF_NULL = "emptyIfNull";
    String NOT_GENERATED_FIELDS = "notGeneratedFields";
    String BUILDER = "Builder";

    interface SymbolsAndFormatting {
        String BACKWARD_SLASH = "\\";
        String STRING_PATTERN = "$S";
        String PATTERN_FOR_GENERIC_INNER_TYPES = ".+<.+<.+";
    }

    interface PathAndPackageConstants {
        String SLASH_MODEL = "\\model";
        String SLASH_MAPPING = "\\mapping";
        String SRC_MAIN_JAVA = "src\\main\\java";
        String BUILD = "build";
        String BUILD_CLASSES_JAVA_MAIN = "build\\classes\\java\\main";
        String DOT_CLASS = ".class";
        String DOT_JAVA = ".java";
        String RU = "ru";
        String MAIN = "main";
        String MODEL = "model";
        String MAPPING = "mapping";
        String DOT_JAR = ".jar";
        String RU_RTI_CRM = "ru.rti.crm";
    }

    interface SupportedJavaClasses {
        String CLASS_STRING = "java.lang.String";
        String CLASS_INTEGER = "java.lang.Integer";
        String CLASS_DOUBLE = "java.lang.Double";
        String CLASS_LONG = "java.lang.Long";
        String CLASS_BYTE = "java.lang.Byte";
        String CLASS_BOOLEAN = "java.lang.Boolean";
        String CLASS_FLOAT = "java.lang.Float";
        String CLASS_DATE = "java.util.Date";
        String CLASS_LIST = "java.util.List";
        String CLASS_SET = "java.util.Set";
        String CLASS_MAP = "java.util.Map";
        String CLASS_QUEUE = "java.util.Queue";
        String CLASS_INTEGER_UNBOX = "int";
        String CLASS_DOUBLE_UNBOX = "double";
        String CLASS_LONG_UNBOX = "long";
        String CLASS_BYTE_UNBOX = "byte";
        String CLASS_BOOLEAN_UNBOX = "boolean";
        String CLASS_FLOAT_UNBOX = "float";
    }
}
