package ru.adk.tarantool.exception;

public class TarantoolConnectionException extends RuntimeException {
    public TarantoolConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TarantoolConnectionException(String message) {
        super(message);
    }
}
