package ru.adk.service.constants;

public interface ServiceLoggingMessages {
    String EXECUTION_SERVICE_MESSAGE = "Executing service: ''{0}.{1}'' with request: ''{2}''";
    String SERVICE_FAILED_MESSAGE = "Service ''{0}.{1}'' execution failed with error: ''{2}: {3}''";
    String SERVICE_EXECUTED_MESSAGE = "Successfully executed service: ''{0}.{1}'' with response: ''{2}''";
    String SERVICE_REGISTRATION_MESSAGE = "Registering service: ''{0}'' with specification class: ''{1}''";
}
