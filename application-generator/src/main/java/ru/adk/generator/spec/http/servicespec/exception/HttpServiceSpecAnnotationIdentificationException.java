package ru.adk.generator.spec.http.servicespec.exception;

/**
 * Thrown to indicate that some error during annotations's
 * type identification occurred.
 */
public class HttpServiceSpecAnnotationIdentificationException extends RuntimeException {

    /**
     * Constructs a <code>HttpServiceSpecAnnotationIdentificationException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public HttpServiceSpecAnnotationIdentificationException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>HttpServiceSpecAnnotationIdentificationException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public HttpServiceSpecAnnotationIdentificationException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>HttpServiceSpecAnnotationIdentificationException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public HttpServiceSpecAnnotationIdentificationException(Exception cause) {
        super(cause);
    }
}
