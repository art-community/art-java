package io.art.meta.constants;

public interface MetaConstants {
    String UNSUPPORTED_TYPE = "Unsupported type: {0}";
    String IS_NAME = "is";
    String GET_NAME = "get";

    enum MetaTypeInternalKind {
        ENTITY,
        ARRAY,
        STRING,
        LONG,
        DOUBLE,
        FLOAT,
        INT,
        BOOL,
        BYTE,
        BINARY,
        LAZY,
        OPTIONAL,
        DATE,
        DATE_TIME,
        SUPPLIER,
        STREAM,
        FLUX,
        MONO,
        ENUM
    }

    static boolean isPrimitive(MetaTypeInternalKind kind) {
        switch (kind) {
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INT:
            case BOOL:
            case BYTE:
                return true;
            default:
                return false;
        }
    }

    enum MetaTypeExternalKind {
        MAP,
        ARRAY,
        STRING,
        LONG,
        DOUBLE,
        FLOAT,
        INT,
        BOOL,
        BYTE,
        BINARY
    }
}
