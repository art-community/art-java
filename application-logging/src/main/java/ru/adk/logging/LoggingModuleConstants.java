package ru.adk.logging;

public interface LoggingModuleConstants {
    String LOGGING_MODULE_ID = "LOGGING_MODULE";
    String DEFAULT_REQUEST_ID = "DEFAULT_REQUEST_ID";
    String PORT = "port";
    String ADDRESS = "address";

    enum LoggingMode {
        CONSOLE,
        SOCKET
    }

    interface LoggingParameters {
        String MODULES_KEY = "modules";
        String SERVICES_KEY = "services";
        String PROTOCOL_KEY = "protocol";
        String TRACE_ID_KEY = "traceId";
        String REQUEST_ID_KEY = "requestId";
        String PROFILE_KEY = "profile";
        String ENVIRONMENT_KEY = "environment";
        String REQUEST_KEY = "request";
        String RESPONSE_KEY = "response";
        String SERVICE_ID_KEY = "serviceId";
        String SERVICE_METHOD_ID_KEY = "serviceMethodId";
        String SERVICE_METHOD_COMMAND_KEY = "serviceMethodCommand";
        String APPLICATION_MODULE_ID_KEY = "applicationModuleId";
        String REQUEST_START_TIME_KEY = "requestStartTime";
        String REQUEST_END_TIME_KEY = "requestEndTime";
        String EXECUTION_TIME_KEY = "executionTime";
        String SERVICE_EXCEPTION_KEY = "serviceException";
        String SERVICE_TYPES_KEY = "serviceTypes";
        String SERVICE_EVENT_TYPE_KEY = "serviceEventType";
        String APPLICATION_JAR_KEY = "applicationJar";
    }
}
