package ru.adk.sql.exception;

public class SqlModuleException extends RuntimeException {
    public SqlModuleException(Exception e) {
        super(e);
    }
}
