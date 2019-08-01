package ru.art.grpc.server.constants;

public interface GrpcServerModuleConstants {
    String GRPC_SERVER_MODULE_ID = "GRPC_SERVER_MODULE";
    String EXECUTE_GRPC_FUNCTION = "EXECUTE_GRPC_FUNCTION";
    String DEFAULT_MODULE_PATH = "/module";
    String GRPC_SERVICE_TYPE = "GRPC";
    String PROTOBUF_ERROR_MESSAGE = "ERROR_MESSAGE";
    String RESPONSE_OK = "OK";
    String TRACE_ID_HEADER = "TRACE_ID";
    String PROFILE_HEADER = "PROFILE";
    String ENVIRONMENT_PROPERTY = "environment";
    String GRPC_SERVER_THREAD = "grpc-server-bootstrap-thread";
    int DEFAULT_MAX_INBOUND_MESSAGE_SIZE = 8 * 1024 * 1024;
    int DEFAULT_HANDSHAKE_TIMEOUT = 60;
}
