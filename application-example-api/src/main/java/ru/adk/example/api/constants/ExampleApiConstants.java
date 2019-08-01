package ru.adk.example.api.constants;

public interface ExampleApiConstants {
    String EXAMPLE_SERVICE_ID = "EXAMPLE_SERVICE";
    String EXAMPLE_GRPC_COMMUNICATION_SERVICE_ID = "EXAMPLE_GRPC_COMMUNICATION_SERVICE_ID";
    String EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID = "EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID";

    interface Methods {
        String REQUEST_RESPONSE_HANDLING_EXAMPLE = "REQUEST_RESPONSE_HANDLING_EXAMPLE";
        String REQUEST_RESPONSE_HANDLING_EXAMPLE_ASYNC = "REQUEST_RESPONSE_HANDLING_EXAMPLE_ASYNC";
        String USING_CONFIGURATION_VALUES_EXAMPLE = "USING_CONFIGURATION_VALUES_EXAMPLE";
        String SOAP_CLIENT_EXAMPLE = "SOAP_CLIENT_EXAMPLE";
        String HTTP_CLIENT_EXAMPLE = "HTTP_CLIENT_EXAMPLE";
        String GRPC_CLIENT_EXAMPLE = "GRPC_CLIENT_EXAMPLE";
        String SQL_EXAMPLE = "SQL_EXAMPLE";
        String ROCKS_DB_EXAMPLE = "ROCKS_DB_EXAMPLE";
        String LOGGING_EXAMPLE = "LOGGING_EXAMPLE";
        String JSON_READ_WRITE_EXAMPLE = "JSON_READ_WRITE_EXAMPLE";
        String PROTOBUF_READ_WRITE_EXAMPLE = "PROTOBUF_READ_WRITE_EXAMPLE";
        String ASYNC_TASK_EXECUTING_EXAMPLE = "ASYNC_TASK_EXECUTING_EXAMPLE";
        String GET_EXAMPLE_MODULE_STATE = "GET_EXAMPLE_MODULE_STATE";
    }

    interface Paths {
        String REQUEST_RESPONSE_HANDLING_EXAMPLE_PATH = "/requestResponseHandlingExample";
        String USING_CONFIGURATION_VALUES_EXAMPLE_PATH = "/usingConfigurationValuesExample";
        String SOAP_CLIENT_EXAMPLE_PATH = "/soapClientExample";
        String HTTP_CLIENT_EXAMPLE_PATH = "/httpClientExample";
        String GRPC_CLIENT_EXAMPLE_PATH = "/grpcClientExample";
        String SQL_EXAMPLE_PATH = "/sqlExample";
        String ROCKS_DB_EXAMPLE_PATH = "/rocksDbExample";
        String LOGGING_EXAMPLE_PATH = "/loggingExample";
        String JSON_READ_WRITE_EXAMPLE_PATH = "/jsonReadWriteExample";
        String PROTOBUF_READ_WRITE_EXAMPLE_PATH = "/protobufReadWriteExample";
        String ASYNC_TASK_EXECUTING_EXAMPLE_PATH = "/asyncTaskExecutingExample";
        String GET_EXAMPLE_MODULE_STATE_PATH = "/getExampleModuleState";
    }

    interface SoapConstants {
        String EXAMPLE_OPERATION = "ExampleOperation";
    }
}
