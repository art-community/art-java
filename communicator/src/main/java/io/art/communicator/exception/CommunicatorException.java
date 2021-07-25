package io.art.communicator.exception;

public class CommunicatorException extends RuntimeException {
    public CommunicatorException(Throwable cause) {
        super(cause);
    }

    public CommunicatorException(String message) {
        super(message);
    }
}
