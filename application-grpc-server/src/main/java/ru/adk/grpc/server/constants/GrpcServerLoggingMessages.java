package ru.adk.grpc.server.constants;

public interface GrpcServerLoggingMessages {
    String GRPC_STARTED_MESSAGE = "GRPC server started in {0}[ms]";
    String GRPC_RESTARTED_MESSAGE = "GRPC Server restarted in {0}[ms]";
    String GRPC_LOADED_SERVICE_MESSAGE = "GRPC service loaded: ''{0}:{1,number,#}'' - ''{2}''.''{3}''.''{4}''";
    String GRPC_ON_MESSAGE = "GRPC onMessage() message:\n''{0}''";
    String GRPC_ON_HALF_CLOSE = "GRPC onHalfClose()";
    String GRPC_ON_CANCEL = "GRPC onCancel()";
    String GRPC_ON_COMPLETE = "GRPC onComplete()";
    String GRPC_ON_READY = "GRPC onReady()";
    String GRPC_LOGGING_EVENT = "grpcServletHandling";
}
