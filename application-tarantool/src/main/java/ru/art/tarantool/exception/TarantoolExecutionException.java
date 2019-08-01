package ru.art.tarantool.exception;

public class TarantoolExecutionException extends RuntimeException {
    public TarantoolExecutionException(Exception e) {
        super(e);
    }

    public TarantoolExecutionException(Throwable e) {
        super(e);
    }
}
