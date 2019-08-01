package ru.adk.generator.spec.common.exception;

/**
 * Thrown to indicate method marked with @Consumes
 * annotation doesn't contain request.
 */
public class MethodConsumesWithoutParamsException extends RuntimeException {

    /**
     * Constructs a <code>MethodConsumesWithoutParamsException</code> with the
     * specified detail message.
     * @param message - message's details.
     */
    public MethodConsumesWithoutParamsException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>MethodConsumesWithoutParamsException</code> with
     * the specified detail message and cause.
     * @param  message - message's details.
     * @param  cause
     */
    public MethodConsumesWithoutParamsException(String message, Exception cause) {
        super(message, cause);
    }

    /** Constructs a <code>MethodConsumesWithoutParamsException</code> with
     * the specified cause.
     * @param cause
     */
    public MethodConsumesWithoutParamsException(Exception cause) {
        super(cause);
    }
}
