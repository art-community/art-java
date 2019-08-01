package ru.art.generator.spec.http.proxyspec.exception;

/**
 * Thrown to indicate that some error during annotations's
 * type identification occurred.
 */
public class HttpProxySpecAnnotationDefinitionException extends RuntimeException {

    /**
     * Constructs a <code>HttpProxySpecAnnotationDefinitionException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public HttpProxySpecAnnotationDefinitionException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>HttpProxySpecAnnotationDefinitionException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public HttpProxySpecAnnotationDefinitionException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>HttpProxySpecAnnotationDefinitionException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public HttpProxySpecAnnotationDefinitionException(Exception cause) {
        super(cause);
    }
}
