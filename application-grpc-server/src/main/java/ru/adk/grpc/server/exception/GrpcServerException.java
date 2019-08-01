package ru.adk.grpc.server.exception;

public class GrpcServerException extends RuntimeException {
    public GrpcServerException(String message, Exception e) {
        super(message, e);
    }
}
