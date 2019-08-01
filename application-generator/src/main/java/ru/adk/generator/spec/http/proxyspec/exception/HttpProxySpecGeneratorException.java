package ru.adk.generator.spec.http.proxyspec.exception;

/**
 * Thrown to indicate that some error during proxy specification's
 * generation occurred.
 */
public class HttpProxySpecGeneratorException extends RuntimeException {

    /**
     * Constructs a <code>HttpProxySpecGeneratorException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public HttpProxySpecGeneratorException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>HttpProxySpecGeneratorException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public HttpProxySpecGeneratorException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>HttpProxySpecGeneratorException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public HttpProxySpecGeneratorException(Exception cause) {
        super(cause);
    }
}
