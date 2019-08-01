package ru.art.sql.exception;

public class SqlModuleException extends RuntimeException {
    public SqlModuleException(Exception e) {
        super(e);
    }
}
