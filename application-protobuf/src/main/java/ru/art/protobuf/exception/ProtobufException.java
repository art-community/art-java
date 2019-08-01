package ru.art.protobuf.exception;

public class ProtobufException extends RuntimeException {
    public ProtobufException(Throwable cause) {
        super(cause);
    }

    public ProtobufException(String message) {
        super(message);
    }
}
