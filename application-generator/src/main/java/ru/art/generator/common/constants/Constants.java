package ru.art.generator.common.constants;

/**
 * Interface for common constants for all generators.
 */
public interface Constants {

    String START_GENERATING = "Start generating ''{0}''";
    String GENERATION_COMPLETED = "Generation completed.";
    String GENERATED_SUCCESSFULLY = "File ''{0}'' has been generated \033[36msuccessfully\033[0m.";
    String XML_MAPPER = "XmlMapper";
    String MAPPER = "Mapper";
    String REQUEST = "Request";
    String RESPONSE = "Response";
    String TO_MODEL = "to";
    String FROM_MODEL = "from";
    String TO_MODEL_FOR_PROXY = "ToModel"; //TODO fix after unifying naming of mappers
    String FROM_MODEL_FOR_PROXY = "FromModel"; //TODO fix after unifying naming of mappers
    String NOT_GENERATED_FIELDS = "notGeneratedFields";
    String ANNOTATION = "@{0}";

    interface PathAndPackageConstants {
        String SRC_MAIN_JAVA = "src\\main\\java";
        String BUILD = "build";
        String RU = "ru";
        String MAIN = "main";
        String DOT_JAR = ".jar";
        String RU_RTI_CRM = "ru.rti.crm";
    }

    interface SymbolsAndFormatting {
        String BACKWARD_SLASH = "\\";
        String VALUE_PATTERN = "$L";
        String STRING_PATTERN = "$S";
        String METHOD_PATTERN = "$N";
    }

    interface PrimitiveMapperConstants {
        String STRING_FROM_MODEL = "StringPrimitive.fromModel";
        String INT_FROM_MODEL = "IntPrimitive.fromModel";
        String LONG_FROM_MODEL = "LongPrimitive.fromModel";
        String DOUBLE_FROM_MODEL = "DoublePrimitive.fromModel";
        String BOOL_FROM_MODEL = "BoolPrimitive.fromModel";
        String BYTE_FROM_MODEL = "BytePrimitive.fromModel";
        String FLOAT_FROM_MODEL = "FloatPrimitive.fromModel";
        String STRING_TO_MODEL = "StringPrimitive.toModel";
        String INT_TO_MODEL = "IntPrimitive.toModel";
        String LONG_TO_MODEL = "LongPrimitive.toModel";
        String DOUBLE_TO_MODEL = "DoublePrimitive.toModel";
        String BOOL_TO_MODEL = "BoolPrimitive.toModel";
        String BYTE_TO_MODEL = "BytePrimitive.toModel";
        String FLOAT_TO_MODEL = "FloatPrimitive.toModel";
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
