package ru.adk.grpc.server.exception;

import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.StatusRuntimeException;
import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;
import static io.grpc.Metadata.Key.of;
import static io.grpc.Status.INTERNAL;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.PROTOBUF_ERROR_MESSAGE;

public class GrpcServletException extends StatusRuntimeException {
    public GrpcServletException(String message) {
        super(INTERNAL, buildMetadata(message));
    }

    private static Metadata buildMetadata(String message) {
        Metadata metadata = new Metadata();
        Key<String> key = of(PROTOBUF_ERROR_MESSAGE, ASCII_STRING_MARSHALLER);
        metadata.put(key, message);
        return metadata;
    }
}
