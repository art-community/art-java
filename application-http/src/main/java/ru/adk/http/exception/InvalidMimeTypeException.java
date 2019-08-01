package ru.adk.http.exception;

import ru.adk.http.constants.HttpMimeTypeExceptionFormat;
import static java.text.MessageFormat.format;

public class InvalidMimeTypeException extends RuntimeException {
    public InvalidMimeTypeException(String value, String message) {
        super(format(HttpMimeTypeExceptionFormat.INVALID_MIME_TYPE_MESSAGE, message, value));
    }
}
