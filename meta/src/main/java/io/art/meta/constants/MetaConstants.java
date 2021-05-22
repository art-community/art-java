package io.art.meta.constants;

public interface MetaConstants {
    String COULD_NOT_CREATE_JAVAC_INSTANCE = "Could not createLogger Javac instance";
    String MODULE_CONFIGURATOR_NOT_FOUND = "Module configurator method not found";
    String MORE_THAN_ONE_PARAMETER = "More than one parameter not supported";
    String UNSUPPORTED_TYPE = "Unsupported type: {0}";
    String TYPE_VARIABLE_WAS_NOT_FOUND = "Type variable was not found: {0}";
    String NOT_PRIMITIVE_TYPE = "Not primitive type: {0}";
    String NOT_COLLECTION_TYPE = "Not collection type: {0}";
    String NOT_FOUND_FACTORY_METHODS = "Not found valid factory methods (deferredExecutor() method, no-args with setters or all-args constructor) for type: {0}";
    String VALIDATION_EXCEPTION_MESSAGE_FORMAT = "Validation exception for: [{0}]\n\t{1}";
    String GENERATION_FAILED_MESSAGE_FORMAT = "Generation failed:\n\t{0}";
    String REPROCESSING_FAILED = "Reprocessing failed";
    String RECOMPILATION_FAILED = "Recompilation failed";
    String NOT_CONFIGURATION_SOURCE_TYPE = "Type is not valid for configuration property: {0}";
    String OVERRIDDEN_METHODS_NOT_SUPPORTED = "Type has overridden methods (rename them to something else): {0}";
    String NOT_FOUND_ALL_ARGS_CONSTRUCTOR = "Not found all-args constructor for type: {0}";
    String IS_NAME = "is";
    String GET_NAME = "get";
    String SET_NAME = "set";
}
