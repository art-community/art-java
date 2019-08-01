package ru.adk.tarantool.exception;

public class TarantoolDaoException extends RuntimeException {
    public TarantoolDaoException(String message) {
        super(message);
    }
}
