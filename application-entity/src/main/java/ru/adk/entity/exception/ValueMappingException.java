package ru.adk.entity.exception;

public class ValueMappingException extends RuntimeException {
    public ValueMappingException(String message) {
        super(message);
    }

    public ValueMappingException(Throwable cause) {
        super(cause);
    }
}
