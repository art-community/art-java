package ru.art.grpc.server.constants;

public interface GrpcServerExceptionMessages {
    String GRPC_SERVER_INITIALIZATION_FAILED = "GRPC server initialization failed";
    String GRPC_SERVER_AWAITING_FAILED = "GRPC server awaiting failed";
    String GRPC_SERVER_RESTART_FAILED = "GRPC server restarting failed";
    String GRPC_SERVLET_INPUT_PARAMETERS_NULL = "GRPC Servlet request or response observer are null";
    String GRPC_SERVICE_NOT_EXISTS_MESSAGE = "GRPC Service with id ''{0}'' not exists";
    String GRPC_METHOD_NOT_EXISTS_MESSAGE = "GRPC Method with id ''{0}'' not exists";
    String GRPC_SERVICE_NOT_EXISTS_CODE = "GRPC_SERVICE_NOT_EXISTS";
    String GRPC_METHOD_NOT_EXISTS_CODE = "GRPC_METHOD_NOT_EXISTS";
    String GRPC_SERVICE_EXCEPTION = "GRPC server service exception";
    String GRPC_SERVLET_ERROR = "GRPC_SERVLET_ERROR";
}
