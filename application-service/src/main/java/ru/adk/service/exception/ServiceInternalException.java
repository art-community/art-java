package ru.adk.service.exception;

public class ServiceInternalException extends RuntimeException {
    public ServiceInternalException(String message) {
        super(message);
    }
}
