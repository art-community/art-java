package ru.art.generator.common.constants;

/**
 * Interface for common constants for exceptions occurred in generators.
 */
public interface ExceptionConstants {
    String UNABLE_TO_PARSE_JAR_PATH = "Unable to parse jar path for ''{0}''";
    String UNABLE_TO_WRITE_TO_FILE = "Unable to write to file ''{0}''";
    String UNABLE_TO_FIND_A_PATH_FOR_CLASS = "Unable to find a path for class ''{0}''. File wasn''t created.";
    String UNABLE_TO_CREATE_FILE_UNKNOWN_ERROR = "Unable to create file ''{0}'' because of the ''{1}''";
    String NOT_SUPPORTED_TYPE_FOR_PRIMITIVE_MAPPER = "''{0}'' - isn''t supported type for getting primitive mapper.";
}
