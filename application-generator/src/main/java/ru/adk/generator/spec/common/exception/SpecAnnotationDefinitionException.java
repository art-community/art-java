package ru.adk.generator.spec.common.exception;

/**
 * Thrown to indicate that some error during annotations's
 * type identification occurred.
 */
public class SpecAnnotationDefinitionException extends RuntimeException {

    /**
     * Constructs a <code>SpecAnnotationDefinitionException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public SpecAnnotationDefinitionException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>SpecAnnotationDefinitionException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public SpecAnnotationDefinitionException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>SpecAnnotationDefinitionException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public SpecAnnotationDefinitionException(Exception cause) {
        super(cause);
    }
}
