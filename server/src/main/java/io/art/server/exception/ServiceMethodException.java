package io.art.server.exception;

public class ServiceMethodException extends RuntimeException {
    public ServiceMethodException(String message) {
        super(message);
    }
}
