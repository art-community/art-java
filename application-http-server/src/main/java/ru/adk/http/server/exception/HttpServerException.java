package ru.adk.http.server.exception;

public class HttpServerException extends RuntimeException {
    public HttpServerException(String message) {
        super(message);
    }

    public HttpServerException(Exception e) {
        super(e);
    }

    public HttpServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
