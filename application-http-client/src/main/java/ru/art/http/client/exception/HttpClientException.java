package ru.art.http.client.exception;

public class HttpClientException extends RuntimeException {
    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(Exception e) {
        super(e);
    }

    public HttpClientException(String message, Exception e) {
        super(message, e);
    }
}
