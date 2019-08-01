package ru.art.generator.mapper.exception;

/**
 * Thrown to indicate that some error during mapping
 * generation occurred.
 */
public class MappingGeneratorException extends RuntimeException {

    /**
     * Constructs a <code>MappingGeneratorException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public MappingGeneratorException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>MappingGeneratorException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public MappingGeneratorException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>MappingGeneratorException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public MappingGeneratorException(Exception cause) {
        super(cause);
    }
}
