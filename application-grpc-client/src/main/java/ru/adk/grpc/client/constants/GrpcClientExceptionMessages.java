package ru.adk.grpc.client.constants;

public interface GrpcClientExceptionMessages {
    String RESPONSE_IS_NULL = "Received response is null";
    String GRPC_CLIENT_EXCEPTION_MESSAGE = "Exception occurred from external module. Please, check logs. GRPC communication configuration: {0}. Error code: {1}. ErrorMessage: {2}";
    String GRPC_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND = "GRPC communication target configuration for service ''{0}'' was not found";
    String GRPC_RESPONSE_MAPPING_MODE_IS_NULL = "GRPC response mapping mode is null";
    String INVALID_GRPC_COMMUNICATION_CONFIGURATION = "Some required fields in GRPC communication configuration are null: ";
}
