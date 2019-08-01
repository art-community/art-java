package ru.adk.service.exception;

public class ChildServiceException extends RuntimeException {
    public ChildServiceException(ServiceExecutionException exception) {
        super(exception);
    }
}
