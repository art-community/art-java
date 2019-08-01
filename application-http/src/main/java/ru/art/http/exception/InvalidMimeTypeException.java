package ru.art.http.exception;

import ru.art.http.constants.HttpMimeTypeExceptionFormat;
import static java.text.MessageFormat.format;

public class InvalidMimeTypeException extends RuntimeException {
    public InvalidMimeTypeException(String value, String message) {
        super(format(HttpMimeTypeExceptionFormat.INVALID_MIME_TYPE_MESSAGE, message, value));
    }
}
