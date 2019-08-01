package ru.adk.generator.mapper.exception;

/**
 * Thrown to indicate that error during generation of
 * inner class. Mainly caused by the fact, that class has
 * NonGenerated annotation or is an enum.
 */
public class InnerClassGenerationException extends MappingGeneratorException {
    public InnerClassGenerationException(String message) {
        super(message);
    }

    public InnerClassGenerationException(String message, Exception cause) {
        super(message, cause);
    }

    public InnerClassGenerationException(Exception cause) {
        super(cause);
    }
}
