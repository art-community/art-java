package ru.art.core.exception;

public class InternalRuntimeException extends RuntimeException {
    public InternalRuntimeException(String message) {
        super(message);
    }

    public InternalRuntimeException(Exception e) {
        super(e);
    }
}
