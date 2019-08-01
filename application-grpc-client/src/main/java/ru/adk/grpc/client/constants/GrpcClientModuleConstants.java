package ru.adk.grpc.client.constants;

public interface GrpcClientModuleConstants {
    String GRPC_COMMUNICATION_SERVICE_TYPE = "GRPC_COMMUNICATION";
    String GRPC_CLIENT_MODULE_ID = "GRPC_CLIENT_MODULE";
    String RESPONSE_OK = "OK";
    String TRACE_ID_HEADER = "TRACE_ID";
    long DEFAULT_TIMEOUT = 10000L;
    int DEFAULT_GRPC_PORT = 8000;
}
