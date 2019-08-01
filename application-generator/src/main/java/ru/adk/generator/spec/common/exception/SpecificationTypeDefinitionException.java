package ru.adk.generator.spec.common.exception;

import ru.adk.generator.spec.common.constants.SpecificationType;

/**
 * Thrown to indicate that some error during specifications's
 * type identification occurred.
 * Possible values presented in {@link SpecificationType}
 */
public class SpecificationTypeDefinitionException extends RuntimeException {

    /**
     * Constructs a <code>SpecificationTypeDefinitionException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public SpecificationTypeDefinitionException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>SpecificationTypeDefinitionException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public SpecificationTypeDefinitionException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>SpecificationTypeDefinitionException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public SpecificationTypeDefinitionException(Exception cause) {
        super(cause);
    }
}
