package io.art.meta.constants;

public interface MetaConstants {
    String IS_NAME = "is";
    String GET_NAME = "get";

    enum MetaTypeInternalKind {
        VOID,
        STRING,
        LONG,
        DOUBLE,
        SHORT,
        FLOAT,
        INTEGER,
        BYTE,
        CHARACTER,
        BOOLEAN,
        DATE,
        LOCAL_DATE_TIME,
        ZONED_DATE_TIME,
        DURATION,

        ARRAY,
        LONG_ARRAY,
        DOUBLE_ARRAY,
        FLOAT_ARRAY,
        INTEGER_ARRAY,
        BOOLEAN_ARRAY,
        CHARACTER_ARRAY,
        SHORT_ARRAY,
        BYTE_ARRAY,

        COLLECTION,
        IMMUTABLE_COLLECTION,
        LIST,
        IMMUTABLE_ARRAY,
        SET,
        IMMUTABLE_SET,
        QUEUE,
        DEQUEUE,
        STREAM,

        MAP,
        IMMUTABLE_MAP,

        FLUX,
        MONO,

        LAZY,
        OPTIONAL,
        SUPPLIER,
        ENUM,
        INPUT_STREAM,
        OUTPUT_STREAM,
        NIO_BUFFER,
        NETTY_BUFFER,
        ENTITY,

        UNKNOWN
    }

    enum MetaTypeExternalKind {
        MAP,
        ARRAY,
        LAZY_MAP,
        LAZY_ARRAY,
        LAZY,
        STRING,
        LONG,
        DOUBLE,
        FLOAT,
        INTEGER,
        BOOLEAN,
        CHARACTER,
        SHORT,
        BYTE,
        BINARY,
        ENTITY,
        UNKNOWN
    }

    enum MetaTypeModifiers {
        VALIDATABLE
    }

    interface Errors {
        String TRANSFORMATION_NOT_AVAILABLE = "Transformation is not available for value: {0} by transformer: {1}";
        String ARRAY_WITH_UNKNOWN_TYPE = "{0} is array with unknown component type";
        String COLLECTION_WITHOUT_PARAMETERS = "{0} is collection without parameters";
        String COLLECTION_WITH_UNKNOWN_TYPE = "{0} is collection with unknown component type";
        String MAP_WITHOUT_PARAMETERS = "{0} is map without parameters";
        String MAP_WITH_UNKNOWN_KEY_TYPE = "{0} is map with unknown key parameter";
        String MAP_WITH_UNKNOWN_VALUE_TYPE = "{0} is map with unknown value parameter";
        String PUBLISHER_WITHOUT_PARAMETERS = "{0} is Publisher without parameters";
        String PUBLISHER_WITH_UNKNOWN_PARAMETER = "{0} is Publisher with unknown parameter";
        String LAZY_WITHOUT_PARAMETERS = "{0} is Lazy without parameters";
        String LAZY_WITH_UNKNOWN_PARAMETER = "{0} is Lazy with unknown parameter";
        String OPTIONAL_WITHOUT_PARAMETERS = "{0} is Optional without parameters";
        String OPTION_WITH_UNKNOWN_PARAMETER = "{0} is Optional with unknown parameter";
        String SUPPLIER_WITHOUT_PARAMETERS = "{0} is Supplier without parameters";
        String SUPPLIER_WITH_UNKNOWN_PARAMETER = "{0} is Supplier with unknown parameter";
        String UNKNOWN_TYPE = "{0} is unknown type";
        String INVOKE_INSTANCE = "invoke(instance)";
        String INVOKE_INSTANCE_ARGUMENT = "invoke(instance, argument)";
        String INVOKE_WITHOUT_ARGUMENTS = "invoke()";
        String INVOKE_ARGUMENT = "invoke(argument)";
        String META_COMPUTATION_FAILED = "Meta computation failed. Errors:\n";
        String INVOCATION_ERROR = "{0} - invocation error: {1}";
        String CLASS_CREATOR_INVALID = "Meta class {0} creator invalid. There is no any available for creation constructors";
        String MAP_WITH_NO_STRING_KEY = "Unable to parse configuration value in map with non-string key type: {0}";
        String UNABLE_TO_CREATE_SINGLETON = "Class {0} hasn't 0-args constructor. We can't use it for method invoker";
        String META_CLASS_FOR_CLASS_NOT_EXISTS = "Class {0} hasn't meta class. Check Meta Library class";
        String PROXY_IS_NULL = "Class {0} has not proxy. Proxy could be created only for interfaces";
    }
}
