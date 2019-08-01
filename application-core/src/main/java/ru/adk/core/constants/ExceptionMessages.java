package ru.adk.core.constants;

public interface ExceptionMessages {
    String EXCEPTION_WRAPPER_ACTION_IS_NULL = "Exception wrapped action is null";
    String EXCEPTION_WRAPPER_FACTORY_IS_NULL = "ExceptionFactory is null";
    String CONTEXT_INITIAL_CONFIGURATION_IS_NULL = "ContextInitialConfiguration is null";
    String MODULE_ID_IS_NULL = "ModuleId is null";
    String MODULE_NOT_LOADED = "Module ''{0}'' was not loaded";
    String MODULE_HAS_NOT_STATE = "Module ''{0}'' hasn't state";
    String CUSTOM_MODULE_CONFIGURATION_IS_NULL = "CustomModuleConfiguration is null";
    String BUILDER_VALIDATOR_HAS_NEXT_ERRORS = "Builder validator has next error fields:";
    String COULD_NOT_FIND_AVAILABLE_PORT_AFTER_ATTEMPTS = "Could not find an available %s port in the range [%d, %d] after %d attempts";
    String COULD_NOT_FIND_AVAILABLE_PORTS_IN_THE_RANGE = "Could not find %d available %s ports in the range [%d, %d]";
}
