package ru.art.rsocket.exception;

public class RsocketClientException extends RuntimeException {
    public RsocketClientException(Exception e) {
        super(e);
    }

    public RsocketClientException(String message) {
        super(message);
    }
}
