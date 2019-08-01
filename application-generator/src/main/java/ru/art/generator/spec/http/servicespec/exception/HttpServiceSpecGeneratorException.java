package ru.art.generator.spec.http.servicespec.exception;

/**
 * Thrown to indicate that some error during specification's
 * generation occurred.
 */
public class HttpServiceSpecGeneratorException extends RuntimeException {

    /**
     * Constructs a <code>HttpServiceSpecGeneratorException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public HttpServiceSpecGeneratorException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>HttpServiceSpecGeneratorException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public HttpServiceSpecGeneratorException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>HttpServiceSpecGeneratorException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public HttpServiceSpecGeneratorException(Exception cause) {
        super(cause);
    }
}
