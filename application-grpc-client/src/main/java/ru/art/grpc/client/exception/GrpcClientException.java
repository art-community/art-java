package ru.art.grpc.client.exception;

public class GrpcClientException extends RuntimeException {
    public GrpcClientException(String message) {
        super(message);
    }

    public GrpcClientException(Exception e) {
        super(e);
    }
}
