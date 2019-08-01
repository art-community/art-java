package ru.art.generator.spec.http.common.exception;

/**
 * Thrown to indicate that some error during mime type's
 * identification occurred.
 */
public class MimeTypeDefinitionException extends RuntimeException {
    /**
     * Constructs a <code>MimeTypeDefinitionException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public MimeTypeDefinitionException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>MimeTypeDefinitionException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public MimeTypeDefinitionException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>MimeTypeDefinitionException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public MimeTypeDefinitionException(Exception cause) {
        super(cause);
    }
}
