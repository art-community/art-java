package ru.art.service.exception;

public class ServiceInternalException extends RuntimeException {
    public ServiceInternalException(String message) {
        super(message);
    }
}
