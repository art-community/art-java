package ru.art.generator.exception;

/**
 * Thrown to indicate that some error during getting
 * primitive mapper by type occurred.
 */
public class NotSupportedTypeForPrimitiveMapperException extends RuntimeException {

    /**
     * Constructs a <code>ExecuteMethodGenerationException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public NotSupportedTypeForPrimitiveMapperException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>ExecuteMethodGenerationException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public NotSupportedTypeForPrimitiveMapperException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>ExecuteMethodGenerationException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public NotSupportedTypeForPrimitiveMapperException(Exception cause) {
        super(cause);
    }
}
