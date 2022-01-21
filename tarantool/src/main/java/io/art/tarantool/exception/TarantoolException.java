package io.art.tarantool.exception;

public class TarantoolException extends RuntimeException {
    public TarantoolException(String message) {
        super(message);
    }

    public TarantoolException(Throwable cause) {
        super(cause);
    }
}
