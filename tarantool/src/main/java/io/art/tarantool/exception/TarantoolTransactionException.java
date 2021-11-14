package io.art.tarantool.exception;

public class TarantoolTransactionException extends RuntimeException{
    public TarantoolTransactionException(String message) {
        super(message);
    }
}
