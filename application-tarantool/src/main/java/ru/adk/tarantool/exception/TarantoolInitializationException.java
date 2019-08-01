package ru.adk.tarantool.exception;

public class TarantoolInitializationException extends RuntimeException {
    public TarantoolInitializationException(String message) {
        super(message);
    }

    public TarantoolInitializationException(Exception e) {
        super(e);
    }
}
