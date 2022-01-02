package io.art.tarantool.exception;

public class TarantoolModuleException extends RuntimeException {
    public TarantoolModuleException(String message) {
        super(message);
    }

    public TarantoolModuleException(Throwable cause) {
        super(cause);
    }
}
