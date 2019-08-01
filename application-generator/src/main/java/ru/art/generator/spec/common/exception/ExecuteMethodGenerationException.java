package ru.art.generator.spec.common.exception;

/**
 * Thrown to indicate that some error during specification's
 * method "executeMethod" generation occurred.
 */
public class ExecuteMethodGenerationException extends RuntimeException {

    /**
     * Constructs a <code>ExecuteMethodGenerationException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public ExecuteMethodGenerationException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>ExecuteMethodGenerationException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public ExecuteMethodGenerationException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>ExecuteMethodGenerationException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public ExecuteMethodGenerationException(Exception cause) {
        super(cause);
    }
}
