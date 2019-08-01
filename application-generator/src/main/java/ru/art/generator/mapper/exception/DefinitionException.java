package ru.art.generator.mapper.exception;

/**
 * Thrown to indicate that error during definition of
 * class or type occurred.
 */
public class DefinitionException extends MappingGeneratorException {
    public DefinitionException(String message) {
        super(message);
    }

    public DefinitionException(String message, Exception cause) {
        super(message, cause);
    }

    public DefinitionException(Exception cause) {
        super(cause);
    }
}
