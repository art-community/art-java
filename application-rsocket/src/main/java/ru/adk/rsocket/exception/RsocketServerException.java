package ru.adk.rsocket.exception;

public class RsocketServerException extends RuntimeException {
    public RsocketServerException(Throwable cause) {
        super(cause);
    }

    public RsocketServerException(String message) {
        super(message);
    }
}
