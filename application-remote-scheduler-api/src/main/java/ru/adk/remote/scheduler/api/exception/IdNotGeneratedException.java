package ru.adk.remote.scheduler.api.exception;

public class IdNotGeneratedException extends RuntimeException {
    public IdNotGeneratedException(String message) {
        super(message);
    }
}
