package ru.art.entity.exception;

public class XmlEntityCreationException extends RuntimeException {
    public XmlEntityCreationException() {
    }

    public XmlEntityCreationException(String message) {
        super(message);
    }

    public XmlEntityCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlEntityCreationException(Throwable cause) {
        super(cause);
    }

    public XmlEntityCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
